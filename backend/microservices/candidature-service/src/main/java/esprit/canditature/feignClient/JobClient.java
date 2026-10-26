package esprit.canditature.feignClient;

import esprit.canditature.dto.JobResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "JOB")
public interface JobClient {

    @GetMapping("/api/jobs/{id}")
    JobResponse findById(@PathVariable("id") Long id);
}
