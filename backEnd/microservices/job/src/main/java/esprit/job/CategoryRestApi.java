package esprit.job;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mic2/categories")
public class CategoryRestApi {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello ";
    }
}
