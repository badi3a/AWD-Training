package esprit.canditature.dto;

import java.time.LocalDate;

/** Réponse complète renvoyée au client : la candidature + le candidat + le job. */
public record CandidatureResponse(Long id, String motivation, LocalDate applicationDate,
                                  Candidate candidate, JobResponse job) {
}
