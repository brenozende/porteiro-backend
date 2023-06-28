package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.dto.AddressDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Address(AddressDTO dto) {
        this.city = dto.getCity();
        this.district = dto.getDistrict();
        this.complement = dto.getComplement();
        this.number = dto.getNumber();
        this.country = dto.getCountry();
        this.postalCode = dto.getPostalCode();
        this.state = dto.getState();
        this.street = dto.getStreet();
    }

}