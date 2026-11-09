package esprit.canditature.dto;

/** JSON reçu de CANDIDAT (les noms doivent être identiques aux clés JSON). */
public record Address(Long id, String street, String houseNumber, String zipCode, Long candidateId) {
}
