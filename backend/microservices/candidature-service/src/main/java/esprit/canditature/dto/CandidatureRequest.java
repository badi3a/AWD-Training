package esprit.canditature.dto;

import java.time.LocalDate;

/** Corps de POST /mic3/candidature. applicationDate est facultative (date du jour par défaut). */
public record CandidatureRequest(Long candidateId, Long jobId, String motivation, LocalDate applicationDate) {
}
