package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Resident;
import lombok.Data;

@Data
public class ResidentDTO {
    private Integer id;
    private String name;
    private String phoneNumber;

    public ResidentDTO(Resident resident) {
        this.id = resident.getId();
        this.name = resident.getName();
        this.phoneNumber = resident.getPhoneNumber();
    }
}
