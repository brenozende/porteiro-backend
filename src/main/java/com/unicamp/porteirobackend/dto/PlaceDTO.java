package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Place;
import lombok.Data;

import java.util.Date;

@Data
public class PlaceDTO {
    private Integer id;
    private String name;
    private Date freeFrom;
    private Date freeUntil;
    private Boolean blocked;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.freeFrom = place.getFreeFrom();
        this.freeUntil = place.getFreeUntil();
        this.blocked = place.getBlocked();
    }
}
