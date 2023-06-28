package com.unicamp.porteirobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    @Temporal(TemporalType.DATE)
    @Column(name = "reservation_from")
    private Date reservationFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "reservation_to")
    private Date reservationTo;

}