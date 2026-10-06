package com.awd.monolith.candidature.controller;

import com.awd.monolith.candidature.dto.ApplicationRequest;
import com.awd.monolith.candidature.dto.ApplicationResponse;
import com.awd.monolith.candidature.dto.ApplicationUpdateRequest;
import com.awd.monolith.candidature.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Tag(name = "Candidatures", description = "Applications of candidates to jobs")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/api/applications")
    @Operation(summary = "List candidatures", description = "Both filters are optional; newest first.")
    @ApiResponse(responseCode = "200", description = "List of candidatures")
    @ApiResponse(responseCode = "404", description = "Candidate or job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public List<ApplicationResponse> findAll(
            @Parameter(description = "Only candidatures of this candidate") @RequestParam(required = false) Long candidateId,
            @Parameter(description = "Only candidatures for this job") @RequestParam(required = false) Long jobId) {
        return applicationService.search(candidateId, jobId);
    }

    @GetMapping("/api/applications/{id}")
    @Operation(summary = "Get a candidature by id")
    @ApiResponse(responseCode = "200", description = "Candidature found")
    @ApiResponse(responseCode = "404", description = "Candidature not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ApplicationResponse findById(@Parameter(description = "Candidature id", example = "1") @PathVariable Long id) {
        return applicationService.findById(id);
    }

    @PostMapping("/api/applications")
    @Operation(summary = "Apply to a job",
            description = "Creates a candidature. The job must be available and a candidate cannot apply twice to the same job.")
    @ApiResponse(responseCode = "201", description = "Candidature created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Candidate or job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Job not available, or candidate already applied",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<ApplicationResponse> create(@Valid @RequestBody ApplicationRequest request) {
        ApplicationResponse created = applicationService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/api/applications/{id}")
    @Operation(summary = "Update a candidature", description = "Only the motivation and the date can be changed.")
    @ApiResponse(responseCode = "200", description = "Candidature updated")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Candidature not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ApplicationResponse update(@Parameter(description = "Candidature id", example = "1") @PathVariable Long id,
                                      @Valid @RequestBody ApplicationUpdateRequest request) {
        return applicationService.update(id, request);
    }

    @DeleteMapping("/api/applications/{id}")
    @Operation(summary = "Delete (withdraw) a candidature")
    @ApiResponse(responseCode = "204", description = "Candidature deleted")
    @ApiResponse(responseCode = "404", description = "Candidature not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@Parameter(description = "Candidature id", example = "1") @PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/candidates/{candidateId}/applications")
    @Operation(summary = "List the candidatures of a candidate")
    @ApiResponse(responseCode = "200", description = "Candidatures of the candidate")
    @ApiResponse(responseCode = "404", description = "Candidate not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public List<ApplicationResponse> findByCandidate(
            @Parameter(description = "Candidate id", example = "1") @PathVariable Long candidateId) {
        return applicationService.search(candidateId, null);
    }

    @GetMapping("/api/jobs/{jobId}/applications")
    @Operation(summary = "List the candidatures received for a job")
    @ApiResponse(responseCode = "200", description = "Candidatures for the job")
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public List<ApplicationResponse> findByJob(
            @Parameter(description = "Job id", example = "1") @PathVariable Long jobId) {
        return applicationService.search(null, jobId);
    }
}
