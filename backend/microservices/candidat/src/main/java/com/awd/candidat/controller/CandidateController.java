package com.awd.candidat.controller;

import com.awd.candidat.dto.CandidateRequest;
import com.awd.candidat.dto.CandidateResponse;
import com.awd.candidat.service.CandidateService;
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
@RequestMapping("/mic1/candidates")
@Tag(name = "Candidates", description = "Manage candidates and link them to an address")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    @Operation(summary = "List all candidates")
    @ApiResponse(responseCode = "200", description = "List of candidates")
    public List<CandidateResponse> findAll() {
        return candidateService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a candidate by id")
    @ApiResponse(responseCode = "200", description = "Candidate found")
    @ApiResponse(responseCode = "404", description = "Candidate not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public CandidateResponse findById(@Parameter(description = "Candidate id", example = "1") @PathVariable Long id) {
        return candidateService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create a candidate", description = "The address is optional and is created together with the candidate.")
    @ApiResponse(responseCode = "201", description = "Candidate created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Email already used",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<CandidateResponse> create(@Valid @RequestBody CandidateRequest request) {
        CandidateResponse created = candidateService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a candidate",
            description = "Updates the candidate's fields. If an address is given, it updates the current address or creates one.")
    @ApiResponse(responseCode = "200", description = "Candidate updated")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Candidate not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Email already used",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public CandidateResponse update(@Parameter(description = "Candidate id", example = "1") @PathVariable Long id,
                                    @Valid @RequestBody CandidateRequest request) {
        return candidateService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a candidate", description = "Also deletes the candidate's address.")
    @ApiResponse(responseCode = "204", description = "Candidate deleted")
    @ApiResponse(responseCode = "404", description = "Candidate not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@Parameter(description = "Candidate id", example = "1") @PathVariable Long id) {
        candidateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/address/{addressId}")
    @Operation(summary = "Assign an existing address to a candidate")
    @ApiResponse(responseCode = "200", description = "Address assigned")
    @ApiResponse(responseCode = "404", description = "Candidate or address not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Address already assigned to another candidate",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public CandidateResponse assignAddress(@Parameter(description = "Candidate id", example = "1") @PathVariable Long id,
                                           @Parameter(description = "Address id", example = "1") @PathVariable Long addressId) {
        return candidateService.assignAddress(id, addressId);
    }

    @DeleteMapping("/{id}/address")
    @Operation(summary = "Unlink the candidate's address", description = "The address itself is kept.")
    @ApiResponse(responseCode = "200", description = "Address unlinked")
    @ApiResponse(responseCode = "404", description = "Candidate not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public CandidateResponse removeAddress(@Parameter(description = "Candidate id", example = "1") @PathVariable Long id) {
        return candidateService.removeAddress(id);
    }
}
