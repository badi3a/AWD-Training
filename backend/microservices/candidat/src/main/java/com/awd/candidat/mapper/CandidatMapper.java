package com.awd.candidat.mapper;

import com.awd.candidat.dto.AddressRequest;
import com.awd.candidat.dto.AddressResponse;
import com.awd.candidat.dto.CandidateRequest;
import com.awd.candidat.dto.CandidateResponse;
import com.awd.candidat.entity.Address;
import com.awd.candidat.entity.Candidate;

/** Converts between entities and DTOs. */
public final class CandidatMapper {

    private CandidatMapper() {
    }

    public static Address toEntity(AddressRequest request) {
        return new Address(request.street(), request.houseNumber(), request.zipCode());
    }

    public static void updateEntity(Address address, AddressRequest request) {
        address.setStreet(request.street());
        address.setHouseNumber(request.houseNumber());
        address.setZipCode(request.zipCode());
    }

    public static AddressResponse toResponse(Address address) {
        if (address == null) {
            return null;
        }
        Long candidateId = address.getCandidate() != null ? address.getCandidate().getId() : null;
        return new AddressResponse(address.getId(), address.getStreet(), address.getHouseNumber(),
                address.getZipCode(), candidateId);
    }

    public static Candidate toEntity(CandidateRequest request) {
        Candidate candidate = new Candidate(request.firstname(), request.lastname(), request.email());
        if (request.address() != null) {
            candidate.setAddress(toEntity(request.address()));
        }
        return candidate;
    }

    public static CandidateResponse toResponse(Candidate candidate) {
        return new CandidateResponse(candidate.getId(), candidate.getFirstname(), candidate.getLastname(),
                candidate.getEmail(), toResponse(candidate.getAddress()));
    }
}
