package com.unicamp.porteirobackend.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String city;
    private String street;
    private String country;
    private String state;
    private String number;
    private String district;
    private String postalCode;
    private String complement;

}
