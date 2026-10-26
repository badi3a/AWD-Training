package com.awd.job.controller;

import com.awd.job.dto.JobRequest;
import com.awd.job.dto.JobResponse;
import com.awd.job.service.JobService;
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
@RequestMapping("/api/jobs")
@Tag(name = "Jobs", description = "Manage job offers")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @Operation(summary = "List jobs", description = "Both filters are optional; newest jobs first.")
    @ApiResponse(responseCode = "200", description = "List of jobs")
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public List<JobResponse> findAll(
            @Parameter(description = "Only open (true) or closed (false) jobs") @RequestParam(required = false) Boolean available,
            @Parameter(description = "Only jobs of this category") @RequestParam(required = false) Long categoryId) {
        return jobService.search(available, categoryId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job by id")
    @ApiResponse(responseCode = "200", description = "Job found")
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public JobResponse findById(@Parameter(description = "Job id", example = "1") @PathVariable Long id) {
        return jobService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a job")
    @ApiResponse(responseCode = "201", description = "Job created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<JobResponse> create(@Valid @RequestBody JobRequest request) {
        JobResponse created = jobService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a job")
    @ApiResponse(responseCode = "200", description = "Job updated")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Job or category not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public JobResponse update(@Parameter(description = "Job id", example = "1") @PathVariable Long id,
                              @Valid @RequestBody JobRequest request) {
        return jobService.update(id, request);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Open or close a job")
    @ApiResponse(responseCode = "200", description = "Availability changed")
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public JobResponse setAvailability(@Parameter(description = "Job id", example = "1") @PathVariable Long id,
                                       @Parameter(description = "New availability", example = "false")
                                       @RequestParam boolean available) {
        return jobService.setAvailability(id, available);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job")
    @ApiResponse(responseCode = "204", description = "Job deleted")
    @ApiResponse(responseCode = "404", description = "Job not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@Parameter(description = "Job id", example = "1") @PathVariable Long id) {
        jobService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
