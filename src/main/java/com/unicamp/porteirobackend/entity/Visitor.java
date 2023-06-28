package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.dto.VisitorDTO;
import com.unicamp.porteirobackend.enums.EAuthType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "visitor")
@NoArgsConstructor
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "document", nullable = false)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private EAuthType authType;

    @Column(name = "relationship")
    private String relationship;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Visitor(VisitorDTO dto, Resident resident) {
        this.name = dto.getName();
        this.document = dto.getDocument();
        this.relationship = dto.getRelationship();
        this.resident = resident;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.authType = EAuthType.CALL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Visitor visitor = (Visitor) o;
        return (getId() != null && Objects.equals(getId(), visitor.getId())) || getDocument().equals(visitor.getDocument());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}