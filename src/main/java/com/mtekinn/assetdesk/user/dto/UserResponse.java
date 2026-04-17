package com.mtekinn.assetdesk.user.dto;

import com.mtekinn.assetdesk.user.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private UserRole role;
}