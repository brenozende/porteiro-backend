package com.unicamp.porteirobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "apartment")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "block")
    private String block;

    @Column(name = "floor")
    private String floor;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "real_estate")
    private String realEstate;

    @ManyToMany
    @JoinTable(name = "resident_apartments",
            joinColumns = @JoinColumn(name = "apartments_id"),
            inverseJoinColumns = @JoinColumn(name = "resident_id"))
    private Set<Resident> residents = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Apartment apartment = (Apartment) o;
        return getId() != null && Objects.equals(getId(), apartment.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}