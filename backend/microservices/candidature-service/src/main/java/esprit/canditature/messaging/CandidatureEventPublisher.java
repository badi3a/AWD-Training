package esprit.canditature.messaging;

import esprit.canditature.dto.Candidate;
import esprit.canditature.dto.JobResponse;
import esprit.canditature.entity.Candidature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Publie les événements de candidature dans RabbitMQ.
 *
 * Le producteur envoie à un EXCHANGE avec une CLÉ DE ROUTAGE, jamais directement à une queue.
 */
@Component
public class CandidatureEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(CandidatureEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public CandidatureEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        // Publisher confirm : RabbitMQ confirme (ack) ou non (nack) avoir pris le message en charge.
        this.rabbitTemplate.setConfirmCallback((correlation, ack, cause) -> {
            String id = correlation != null ? correlation.getId() : "?";
            if (ack) {
                log.debug("RabbitMQ a confirmé l'événement {}", id);
            } else {
                log.error("RabbitMQ n'a PAS confirmé l'événement {} : {}", id, cause);
            }
        });

        // Publisher return : l'exchange a reçu le message mais aucune queue n'y est liée.
        this.rabbitTemplate.setReturnsCallback(returned ->
                log.warn("Événement non routable (aucune queue liée à '{}') : {}",
                        returned.getRoutingKey(), returned.getReplyText()));
    }

    /**
     * Annonce qu'une candidature a été déposée.
     * Appelée APRÈS l'enregistrement en base : on n'annonce jamais un fait qui n'a pas eu lieu.
     */
    public void publishApplicationSubmitted(Candidature candidature, Candidate candidate, JobResponse job) {
        ApplicationSubmittedEvent event = ApplicationSubmittedEvent.of(candidature, candidate, job);
        try {
            rabbitTemplate.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.APPLICATION_SUBMITTED_KEY,
                    event,
                    message -> {
                        message.getMessageProperties().setMessageId(event.eventId());
                        message.getMessageProperties().setType(event.eventType());
                        return message;
                    },
                    new CorrelationData(event.eventId()));
            log.info("Événement {} publié : candidature {} (eventId={})",
                    event.eventType(), event.applicationId(), event.eventId());
        } catch (AmqpException ex) {
            // RabbitMQ indisponible : la candidature est enregistrée mais l'événement est perdu.
            // Limite assumée du scénario pilote -> solution robuste : le pattern Outbox (bonus).
            log.error("Échec de publication de l'événement {} pour la candidature {} : {}",
                    event.eventType(), event.applicationId(), ex.getMessage());
        }
    }
}
