package com.awd.monolith.candidature.entity;

import com.awd.monolith.candidat.entity.Candidate;
import com.awd.monolith.job.entity.Job;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * A candidature (application): links one candidate to one job.
 * <p>
 * Monolith: real foreign keys to the candidate and job tables (JPA @ManyToOne).
 * Microservices: these become plain ids (candidateId, jobId) and the data is fetched over REST.
 */
@Entity
@Table(name = "application",
        uniqueConstraints = @UniqueConstraint(name = "uk_application_candidate_job", columnNames = {"candidate_id", "job_id"}))
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(length = 2000)
    private String motivation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    public Application() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getApplicationDate() { return applicationDate; }
    public void setApplicationDate(LocalDate applicationDate) { this.applicationDate = applicationDate; }

    public String getMotivation() { return motivation; }
    public void setMotivation(String motivation) { this.motivation = motivation; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }

    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
}
