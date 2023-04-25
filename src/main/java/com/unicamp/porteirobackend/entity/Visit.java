package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "visit")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private VisitStatus visitStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    @JdbcTypeCode(SqlTypes.DATE)
    private Date date;

}