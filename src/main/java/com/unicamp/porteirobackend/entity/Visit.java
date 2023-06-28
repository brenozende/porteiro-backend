package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.enums.EVisitStatus;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_status")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private EVisitStatus visitStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updatedAt;

}