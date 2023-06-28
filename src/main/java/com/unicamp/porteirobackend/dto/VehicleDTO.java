package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleDTO {
    private Integer id;
    private String manufacturer;
    private String model;
    private String license;
    private String color;

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.manufacturer = vehicle.getManufacturer();
        this.model = vehicle.getModel();
        this.license = vehicle.getLicense();
        this.color = vehicle.getColor();
    }
}
