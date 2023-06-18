package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Place;
import lombok.Data;

@Data
public class PlaceDTO {
    private Integer id;
    private String name;
    private Boolean blocked;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.blocked = place.getBlocked();
    }
}
