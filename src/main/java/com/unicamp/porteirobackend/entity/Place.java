package com.unicamp.porteirobackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "free_from")
    private Date freeFrom;

    @Temporal(TemporalType.DATE)
    @Column(name = "free_until")
    private Date freeUntil;

    @Column(name = "blocked")
    private Boolean blocked = false;

}