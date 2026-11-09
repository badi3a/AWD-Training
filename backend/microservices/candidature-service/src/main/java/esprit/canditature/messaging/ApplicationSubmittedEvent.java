package esprit.canditature.messaging;

import esprit.canditature.dto.Candidate;
import esprit.canditature.dto.JobResponse;
import esprit.canditature.entity.Candidature;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Événement « une candidature a été déposée ».
 *
 * - Un événement décrit un FAIT ACCOMPLI (verbe au passé) : il ne donne pas d'ordre.
 * - Il transporte les données utiles aux consommateurs (e-mail, nom du job...) :
 *   notification n'a pas besoin de rappeler CANDIDAT ni JOB (« event-carried state transfer »).
 * - eventId est unique : il permet au consommateur d'ignorer un doublon (idempotence).
 */
public record ApplicationSubmittedEvent(
        String eventId,
        String eventType,
        Instant occurredAt,
        Long applicationId,
        LocalDate applicationDate,
        Long candidateId,
        String candidateFirstname,
        String candidateLastname,
        String candidateEmail,
        Long jobId,
        String jobName) {

    public static final String TYPE = "ApplicationSubmitted";

    public static ApplicationSubmittedEvent of(Candidature candidature, Candidate candidate, JobResponse job) {
        return new ApplicationSubmittedEvent(
                UUID.randomUUID().toString(),
                TYPE,
                Instant.now(),
                candidature.getId(),
                candidature.getApplicationDate(),
                candidate.id(),
                candidate.firstname(),
                candidate.lastname(),
                candidate.email(),
                job.id(),
                job.name());
    }
}
