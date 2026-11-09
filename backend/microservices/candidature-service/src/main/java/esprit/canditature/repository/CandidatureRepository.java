package esprit.canditature.repository;

import esprit.canditature.entity.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    boolean existsByCandidateIdAndJobId(Long candidateId, Long jobId);
}
