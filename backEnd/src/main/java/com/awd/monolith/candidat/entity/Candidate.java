package com.awd.monolith.candidat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Owning side of the one-to-one relationship: candidate.address_id -> address.id.
     * Saving or deleting a candidate also saves or deletes its address.
     * Unlinking an address from a candidate keeps the address in the database.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", unique = true)
    private Address address;

    public Candidate() {
    }

    public Candidate(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    /** Keeps both sides of the relationship in sync. */
    public void setAddress(Address address) {
        if (this.address != null) {
            this.address.setCandidate(null);
        }
        this.address = address;
        if (address != null) {
            address.setCandidate(this);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Address getAddress() { return address; }
}
