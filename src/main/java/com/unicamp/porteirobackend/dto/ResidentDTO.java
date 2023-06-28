package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Resident;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResidentDTO {
    private Integer id;
    private String name;
    private String phoneNumber;
    private List<VehicleDTO> vehicles;

    public ResidentDTO(Resident resident) {
        this.id = resident.getId();
        this.name = resident.getName();
        this.phoneNumber = resident.getPhoneNumber();
        if (!resident.getVehicles().isEmpty())
            this.vehicles = resident.getVehicles().stream().map(VehicleDTO::new).collect(Collectors.toList());
    }
}
