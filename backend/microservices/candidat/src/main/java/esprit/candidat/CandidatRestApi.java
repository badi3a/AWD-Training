package esprit.candidat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RefreshScope
@RestController
@RequestMapping("mic1/candidats")
public class CandidatRestApi {
    //simple web service for testing
    @Value("${welcome.message}")
    private String welcomemessage;
    @GetMapping("/hello")
    public String sayHello() {
        return welcomemessage;
    }
    @Autowired
    private CandidatService candidatService;
    // GET ALL
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Candidat>> listCandidat() {
        return new ResponseEntity<>(candidatService.findAll(), HttpStatus.OK);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Candidat> getCandidat(@PathVariable int id) {
        return ResponseEntity.ok(candidatService.findById(id));
    }

    // CREATE
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Candidat> addCandidat(@RequestBody Candidat candidat) {
        return new ResponseEntity<>(candidatService.save(candidat), HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Candidat> updateCandidat(
            @PathVariable int id,
            @RequestBody Candidat candidat) {
        return ResponseEntity.ok(candidatService.update(id, candidat));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidat(@PathVariable int id) {
        candidatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
