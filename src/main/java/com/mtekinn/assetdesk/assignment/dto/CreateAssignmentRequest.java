package com.mtekinn.assetdesk.assignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAssignmentRequest {

    @NotNull(message = "Asset id is required")
    private Long assetId;

    @NotNull(message = "User id is required")
    private Long userId;

    private String note;
}