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
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Le service publie l'événement uniquement quand la candidature est réellement enregistrée. */
class CandidatureServiceTest {

    private final CandidatureRepository repository = mock(CandidatureRepository.class);
    private final CandidatClient candidatClient = mock(CandidatClient.class);
    private final JobClient jobClient = mock(JobClient.class);
    private final CandidatureEventPublisher publisher = mock(CandidatureEventPublisher.class);
    private final CandidatureService service = new CandidatureService(repository, candidatClient, jobClient, publisher);

    private final Candidate candidate = new Candidate(1L, "Badia", "Abouhdid", "badia@example.com", null);
    private final CandidatureRequest request = new CandidatureRequest(1L, 1L, "Spring Cloud", null);

    private static JobResponse job(boolean available) {
        return new JobResponse(1L, "Java Spring Boot Developer", "", available, LocalDate.of(2026, 9, 28), null);
    }

    @Test
    void savedApplicationIsAnnounced() {
        when(candidatClient.findById(1L)).thenReturn(candidate);
        when(jobClient.findById(1L)).thenReturn(job(true));
        when(repository.save(any(Candidature.class))).thenAnswer(inv -> {
            Candidature c = inv.getArgument(0);
            c.setId(6L);
            return c;
        });

        CandidatureResponse response = service.add(request);

        assertThat(response.id()).isEqualTo(6L);
        assertThat(response.applicationDate()).isEqualTo(LocalDate.now());
        verify(publisher).publishApplicationSubmitted(any(Candidature.class), any(Candidate.class), any(JobResponse.class));
    }

    @Test
    void closedJobIsRejectedAndNothingIsAnnounced() {
        when(candidatClient.findById(1L)).thenReturn(candidate);
        when(jobClient.findById(1L)).thenReturn(job(false));

        assertThatThrownBy(() -> service.add(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("plus disponible");
        verify(repository, never()).save(any());
        verify(publisher, never()).publishApplicationSubmitted(any(), any(), any());
    }

    @Test
    void duplicateIsRejectedAndNothingIsAnnounced() {
        when(candidatClient.findById(1L)).thenReturn(candidate);
        when(jobClient.findById(1L)).thenReturn(job(true));
        when(repository.existsByCandidateIdAndJobId(1L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.add(request)).isInstanceOf(ResponseStatusException.class);
        verify(publisher, never()).publishApplicationSubmitted(any(), any(), any());
    }
}
