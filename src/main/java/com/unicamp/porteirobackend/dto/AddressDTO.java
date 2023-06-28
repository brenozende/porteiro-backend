package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {
    private Integer id;
    private String city;
    private String street;
    private String country;
    private String state;
    private String number;
    private String district;
    private String postalCode;
    private String complement;

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.city = address.getCity();
        this.street = address.getStreet();
        this.country = address.getCountry();
        this.complement = address.getComplement();
        this.number = address.getNumber();
        this.district = address.getDistrict();
        this.postalCode = address.getPostalCode();
        this.state = address.getState();
    }
}
