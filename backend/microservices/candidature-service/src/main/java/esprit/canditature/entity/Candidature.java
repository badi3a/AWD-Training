package esprit.canditature.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

/**
 * Une candidature ne stocke que les identifiants du candidat et du job :
 * ces objets appartiennent aux microservices CANDIDAT et JOB (pas de clé étrangère).
 */
@Entity
@Table(name = "candidature",
        uniqueConstraints = @UniqueConstraint(columnNames = {"candidate_id", "job_id"}))
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(nullable = false, length = 1000)
    private String motivation;

    private LocalDate applicationDate;

    public Candidature() {
    }

    public Candidature(Long candidateId, Long jobId, String motivation, LocalDate applicationDate) {
        this.candidateId = candidateId;
        this.jobId = jobId;
        this.motivation = motivation;
        this.applicationDate = applicationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }
}
