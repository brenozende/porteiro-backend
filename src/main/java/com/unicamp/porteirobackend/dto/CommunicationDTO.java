package com.unicamp.porteirobackend.dto;

import com.unicamp.porteirobackend.entity.Communications;
import lombok.Data;

import java.util.Date;

@Data
public class CommunicationDTO {
    private Integer id;
    private Date date;
    private String fromUser;
    private String message;

    public CommunicationDTO(Communications communications) {
        this.setId(communications.getId());
        this.setFromUser(communications.getFromUser().getUsername());
        this.setDate(communications.getDate());
        this.setMessage(communications.getMessage());
    }
}
