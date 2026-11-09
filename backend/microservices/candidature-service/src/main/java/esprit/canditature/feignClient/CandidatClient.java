package esprit.canditature.feignClient;

import esprit.canditature.dto.Candidate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Client OpenFeign : "CANDIDAT" est le nom du service dans Eureka, pas une URL. */
@FeignClient(name = "CANDIDAT")
public interface CandidatClient {

    @GetMapping("/mic1/candidates/{id}")
    Candidate findById(@PathVariable("id") Long id);
}
