package com.unicamp.porteirobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "resident")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "document")
    private String document;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address mailAddress;

    @OneToMany(mappedBy = "resident", orphanRemoval = true)
    private Set<EmergencyContact> emergencyContacts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "resident", orphanRemoval = true)
    private Set<Visitor> visitors = new LinkedHashSet<>();

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "owner")
    private boolean owner;

    @ManyToMany
    @JoinTable(name = "resident_apartments",
            joinColumns = @JoinColumn(name = "resident_id"),
            inverseJoinColumns = @JoinColumn(name = "apartments_id"))
    private Set<Apartment> apartments = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "resident_id")
    private Set<Vehicle> vehicles = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Resident resident = (Resident) o;
        return getId() != null && Objects.equals(getId(), resident.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}