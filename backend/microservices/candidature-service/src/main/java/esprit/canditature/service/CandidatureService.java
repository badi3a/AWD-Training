package esprit.canditature.service;

import esprit.canditature.dto.Candidate;
import esprit.canditature.dto.CandidatureRequest;
import esprit.canditature.dto.CandidatureResponse;
import esprit.canditature.dto.JobResponse;
import esprit.canditature.entity.Candidature;
import esprit.canditature.feignClient.CandidatClient;
import esprit.canditature.feignClient.JobClient;
import esprit.canditature.messaging.CandidatureEventPublisher;
import esprit.canditature.repository.CandidatureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class CandidatureService {

    private final CandidatureRepository repository;
    private final CandidatClient candidatClient;
    private final JobClient jobClient;
    private final CandidatureEventPublisher eventPublisher;   // chapitre 6

    public CandidatureService(CandidatureRepository repository, CandidatClient candidatClient,
                              JobClient jobClient, CandidatureEventPublisher eventPublisher) {
        this.repository = repository;
        this.candidatClient = candidatClient;
        this.jobClient = jobClient;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Postuler à un job.
     * 1-2. appels SYNCHRONES (OpenFeign) : on a besoin de la réponse pour décider ;
     * 3-5. règles métier ; 6. enregistrement ;
     * 7. événement ASYNCHRONE : on annonce le fait, sans attendre notification.
     */
    public CandidatureResponse add(CandidatureRequest request) {
        validate(request);

        Candidate candidate = candidatClient.findById(request.candidateId()); // 404 -> FeignException.NotFound
        JobResponse job = jobClient.findById(request.jobId());

        if (!Boolean.TRUE.equals(job.available())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce job n'est plus disponible");
        }
        if (repository.existsByCandidateIdAndJobId(request.candidateId(), request.jobId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce candidat a déjà postulé à ce job");
        }

        LocalDate date = request.applicationDate() != null ? request.applicationDate() : LocalDate.now();
        Candidature saved = repository.save(
                new Candidature(request.candidateId(), request.jobId(), request.motivation(), date));

        // Chapitre 6 : publier APRÈS l'enregistrement. La réponse 201 n'attend pas notification.
        eventPublisher.publishApplicationSubmitted(saved, candidate, job);

        return toResponse(saved, candidate, job);
    }

    public CandidatureResponse findById(Long id) {
        Candidature candidature = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable"));
        return enrich(candidature);
    }

    public List<CandidatureResponse> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidature introuvable");
        }
        repository.deleteById(id);
    }

    private CandidatureResponse enrich(Candidature candidature) {
        Candidate candidate = candidatClient.findById(candidature.getCandidateId());
        JobResponse job = jobClient.findById(candidature.getJobId());
        return toResponse(candidature, candidate, job);
    }

    private static CandidatureResponse toResponse(Candidature c, Candidate candidate, JobResponse job) {
        return new CandidatureResponse(c.getId(), c.getMotivation(), c.getApplicationDate(), candidate, job);
    }

    private static void validate(CandidatureRequest request) {
        if (request == null || request.candidateId() == null || request.jobId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "candidateId et jobId sont obligatoires");
        }
        if (request.motivation() == null || request.motivation().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La motivation est obligatoire");
        }
        if (request.applicationDate() != null && request.applicationDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La date de candidature ne peut pas être dans le futur");
        }
    }
}
