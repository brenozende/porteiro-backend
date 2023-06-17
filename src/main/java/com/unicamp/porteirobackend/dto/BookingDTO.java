package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Booking;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
public class BookingDTO {
    private Integer id;
    private PlaceDTO place;
    private ResidentDTO resident;
    private Date reservedFrom;
    private Date reservedUntil;

    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.place = new PlaceDTO(booking.getPlace());
        this.resident = new ResidentDTO(booking.getResident());
        this.reservedFrom = booking.getReservationFrom();
        this.reservedUntil = booking.getReservationTo();
    }
}
