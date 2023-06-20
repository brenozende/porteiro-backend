package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Visitor;
import com.unicamp.porteirobackend.enums.EAuthType;
import lombok.Data;

@Data
public class VisitorDTO {
    private Integer id;
    private String name;
    private String document;
    private String relationship;
    private EAuthType authType;

    public VisitorDTO(Visitor visitor) {
        this.id = visitor.getId();
        this.name = visitor.getName();
        this.document = visitor.getDocument();
        this.relationship = visitor.getRelationship();
        this.authType = visitor.getAuthType();
    }
}
