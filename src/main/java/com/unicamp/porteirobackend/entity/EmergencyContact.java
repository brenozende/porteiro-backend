package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.dto.EmergencyContactDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "emergency_contact")
@NoArgsConstructor
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

    public EmergencyContact(EmergencyContactDTO dto, Resident resident) {
        this.name = dto.getName();
        this.phoneNumber = dto.getPhoneNumber();
        this.resident = resident;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EmergencyContact that = (EmergencyContact) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}