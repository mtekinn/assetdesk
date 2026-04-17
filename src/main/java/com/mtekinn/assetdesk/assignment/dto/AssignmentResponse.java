package com.mtekinn.assetdesk.assignment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentResponse {

    private Long id;
    private Long assetId;
    private String assetCode;
    private Long userId;
    private String userFullName;
    private LocalDateTime assignedAt;
    private LocalDateTime returnedAt;
    private String note;
}