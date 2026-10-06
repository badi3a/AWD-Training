package com.awd.monolith.candidat.service;

import com.awd.monolith.candidat.dto.AddressRequest;
import com.awd.monolith.candidat.dto.AddressResponse;
import com.awd.monolith.candidat.entity.Address;
import com.awd.monolith.common.exception.ResourceNotFoundException;
import com.awd.monolith.candidat.mapper.CandidatMapper;
import com.awd.monolith.candidat.repository.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> findAll() {
        return addressRepository.findAll().stream().map(CandidatMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AddressResponse findById(Long id) {
        return CandidatMapper.toResponse(getAddress(id));
    }

    public AddressResponse create(AddressRequest request) {
        return CandidatMapper.toResponse(addressRepository.save(CandidatMapper.toEntity(request)));
    }

    public AddressResponse update(Long id, AddressRequest request) {
        Address address = getAddress(id);
        CandidatMapper.updateEntity(address, request);
        return CandidatMapper.toResponse(address);
    }

    public void delete(Long id) {
        Address address = getAddress(id);
        if (address.getCandidate() != null) {
            // unlink first so the candidate's foreign key does not point to a deleted row
            address.getCandidate().setAddress(null);
        }
        addressRepository.delete(address);
    }

    Address getAddress(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address", id));
    }
}
