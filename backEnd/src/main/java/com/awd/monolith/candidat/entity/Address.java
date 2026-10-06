package com.awd.monolith.candidat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    /** Inverse side of the one-to-one relationship (Candidate owns the foreign key). */
    @OneToOne(mappedBy = "address")
    private Candidate candidate;

    public Address() {
    }

    public Address(String street, String houseNumber, String zipCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public Candidate getCandidate() { return candidate; }
    public void setCandidate(Candidate candidate) { this.candidate = candidate; }
}
