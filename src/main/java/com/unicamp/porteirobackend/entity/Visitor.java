package com.unicamp.porteirobackend.entity;

import com.unicamp.porteirobackend.enums.AuthType;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "visitor")
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "document", nullable = false)
    private String document;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type")
    private AuthType authType;

//    @Column(name = "restrictions")
//    private Map<String, String> restrictions;

    @ManyToMany
    @JoinTable(name = "visitor_users",
            joinColumns = @JoinColumn(name = "visitor_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Set<User> users = new LinkedHashSet<>();

}