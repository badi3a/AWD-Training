package esprit.candidat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mic1/candidats")
public class CandidatRestApi {
    //simple web service for testing
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello I'm Microservice 1: " +
                "I work on Candidat and Adress Entities";
    }

}
