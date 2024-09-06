package com.project.ens.dto;

import com.project.ens.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private UserRole role;
}
