package esprit.canditature.dto;

import java.time.LocalDate;

/** JSON reçu de JOB : GET /mic2/jobs/{id}. */
public record JobResponse(Long id, String name, String description, Boolean available,
                          LocalDate date, CategoryResponse category) {
}
