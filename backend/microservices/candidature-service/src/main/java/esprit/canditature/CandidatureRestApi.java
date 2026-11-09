package esprit.canditature;

import esprit.canditature.dto.Candidate;
import esprit.canditature.dto.CandidatureRequest;
import esprit.canditature.dto.CandidatureResponse;
import esprit.canditature.dto.JobResponse;
import esprit.canditature.feignClient.CandidatClient;
import esprit.canditature.feignClient.JobClient;
import esprit.canditature.service.CandidatureService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mic3/candidature")
public class CandidatureRestApi {

    private final CandidatureService service;
    private final CandidatClient candidatClient;
    private final JobClient jobClient;

    public CandidatureRestApi(CandidatureService service, CandidatClient candidatClient, JobClient jobClient) {
        this.service = service;
        this.candidatClient = candidatClient;
        this.jobClient = jobClient;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Im microservice 3!";
    }

    /** Démo Feign : lire un candidat dans CANDIDAT. */
    @GetMapping("/candidat/{id}")
    public Candidate candidat(@PathVariable Long id) {
        return candidatClient.findById(id);
    }

    /** Démo Feign : lire un job dans JOB. */
    @GetMapping("/job/{id}")
    public JobResponse job(@PathVariable Long id) {
        return jobClient.findById(id);
    }

    @PostMapping
    public ResponseEntity<CandidatureResponse> add(@RequestBody CandidatureRequest request) {
        CandidatureResponse created = service.add(request);
        return ResponseEntity.created(URI.create("/mic3/candidature/" + created.id())).body(created);
    }

    @GetMapping("/{id}")
    public CandidatureResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public List<CandidatureResponse> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Traduction des erreurs distantes (chapitre 4) ----------

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, Object>> remoteNotFound(FeignException ex, HttpServletRequest req) {
        String detail = ex.request() != null ? ex.request().url() : ex.getMessage();
        return error(HttpStatus.NOT_FOUND, "Candidat ou job introuvable : " + detail, req);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> remoteUnavailable(FeignException ex, HttpServletRequest req) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "Microservice CANDIDAT ou JOB indisponible : " + ex.getMessage(), req);
    }

    /** Deux POST simultanés passent la vérification : la contrainte d'unicité de la base tranche. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> duplicate(HttpServletRequest req) {
        return error(HttpStatus.CONFLICT, "Ce candidat a déjà postulé à ce job", req);
    }

    private static ResponseEntity<Map<String, Object>> error(HttpStatus status, String message, HttpServletRequest req) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
