package esprit.canditature.messaging;

import esprit.canditature.dto.Candidate;
import esprit.canditature.dto.CategoryResponse;
import esprit.canditature.dto.JobResponse;
import esprit.canditature.entity.Candidature;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.net.ConnectException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/** Test unitaire : ni RabbitMQ ni Spring ne sont démarrés (RabbitTemplate est simulé). */
class CandidatureEventPublisherTest {

    private final RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
    private final CandidatureEventPublisher publisher = new CandidatureEventPublisher(rabbitTemplate);

    private final Candidate candidate = new Candidate(1L, "Badia", "Abouhdid", "badia@example.com", null);
    private final JobResponse job = new JobResponse(1L, "Java Spring Boot Developer", "Build microservices",
            true, LocalDate.of(2026, 9, 28), new CategoryResponse(1L, "Software Development", null));

    private Candidature savedCandidature() {
        Candidature c = new Candidature(1L, 1L, "Motivation", LocalDate.of(2026, 10, 18));
        c.setId(6L);
        return c;
    }

    @Test
    void publishesToTheExchangeWithTheRoutingKey() {
        publisher.publishApplicationSubmitted(savedCandidature(), candidate, job);

        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(rabbitTemplate).convertAndSend(eq("jobboard.events"), eq("application.submitted"),
                payload.capture(), any(MessagePostProcessor.class), any(CorrelationData.class));

        ApplicationSubmittedEvent event = (ApplicationSubmittedEvent) payload.getValue();
        assertThat(event.eventType()).isEqualTo("ApplicationSubmitted");
        assertThat(event.eventId()).isNotBlank();
        assertThat(event.applicationId()).isEqualTo(6L);
        assertThat(event.candidateEmail()).isEqualTo("badia@example.com");
        assertThat(event.jobName()).isEqualTo("Java Spring Boot Developer");
    }

    @Test
    void brokerDownDoesNotBreakTheApplication() {
        doThrow(new AmqpConnectException(new ConnectException("Connection refused")))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Object.class),
                        any(MessagePostProcessor.class), any(CorrelationData.class));

        assertThatCode(() -> publisher.publishApplicationSubmitted(savedCandidature(), candidate, job))
                .doesNotThrowAnyException();
    }
}
