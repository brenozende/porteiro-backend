package com.unicamp.porteirobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "number")
    private String number;

    @Column(name = "district")
    private String district;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "complement")
    private String complement;

}