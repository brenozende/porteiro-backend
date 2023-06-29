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
    private String document;
    private List<VehicleDTO> vehicles;
    private List<VisitorDTO> visitors;

    public ResidentDTO(Resident resident) {
        this.id = resident.getId();
        this.name = resident.getName();
        this.phoneNumber = resident.getPhoneNumber();
        this.document = resident.getDocument();
        if (!resident.getVehicles().isEmpty())
            this.vehicles = resident.getVehicles().stream().map(VehicleDTO::new).collect(Collectors.toList());
        if (!resident.getVisitors().isEmpty())
            this.visitors = resident.getVisitors().stream().map(visitor -> new VisitorDTO(visitor, false)).collect(Collectors.toList());
    }
}
