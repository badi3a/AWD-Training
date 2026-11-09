package com.awd.candidat.controller;

import com.awd.candidat.dto.AddressRequest;
import com.awd.candidat.dto.AddressResponse;
import com.awd.candidat.service.AddressService;
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
@RequestMapping("/mic1/addresses")
@Tag(name = "Addresses", description = "Manage addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "List all addresses")
    @ApiResponse(responseCode = "200", description = "List of addresses")
    public List<AddressResponse> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an address by id")
    @ApiResponse(responseCode = "200", description = "Address found")
    @ApiResponse(responseCode = "404", description = "Address not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public AddressResponse findById(@Parameter(description = "Address id", example = "1") @PathVariable Long id) {
        return addressService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create an address", description = "Creates an address not yet linked to any candidate.")
    @ApiResponse(responseCode = "201", description = "Address created")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest request) {
        AddressResponse created = addressService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an address")
    @ApiResponse(responseCode = "200", description = "Address updated")
    @ApiResponse(responseCode = "400", description = "Invalid input",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Address not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public AddressResponse update(@Parameter(description = "Address id", example = "1") @PathVariable Long id,
                                  @Valid @RequestBody AddressRequest request) {
        return addressService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an address", description = "If a candidate uses this address, it is unlinked first.")
    @ApiResponse(responseCode = "204", description = "Address deleted")
    @ApiResponse(responseCode = "404", description = "Address not found",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<Void> delete(@Parameter(description = "Address id", example = "1") @PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
