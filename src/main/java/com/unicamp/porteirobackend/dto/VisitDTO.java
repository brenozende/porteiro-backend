package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Visit;
import com.unicamp.porteirobackend.enums.EVisitStatus;
import lombok.Data;

import java.util.Date;

@Data
public class VisitDTO {
    private Integer id;
    private VisitorDTO visitor;
    private EVisitStatus visitStatus;
    private Date createdAt;
    private Date updatedAt;

    public VisitDTO(Visit visit) {
        this.id = visit.getId();
        this.visitor = new VisitorDTO(visit.getVisitor());
        this.visitStatus = visit.getVisitStatus();
        this.createdAt = visit.getCreatedAt();
        this.updatedAt = visit.getUpdatedAt();
    }
}
