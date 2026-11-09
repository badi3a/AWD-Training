package esprit.canditature.dto;

/** JSON reçu de CANDIDAT : GET /mic1/candidates/{id}. */
public record Candidate(Long id, String firstname, String lastname, String email, Address address) {
}
