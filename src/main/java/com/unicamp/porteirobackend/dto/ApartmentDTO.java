package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Apartment;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ApartmentDTO {
    private Integer id;
    private Integer number;
    private String floor;
    private AddressDTO address;
    private String block;
    private String realEstate;

    public ApartmentDTO(Apartment apartment){
        this.id = apartment.getId();
        this.number = apartment.getNumber();
        this.floor = apartment.getFloor();
        this.address = new AddressDTO(apartment.getAddress());
        this.block = apartment.getBlock();
        this.realEstate = apartment.getRealEstate();
    }
}
