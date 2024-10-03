package com.project.ens.dto;

import com.project.ens.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @Size(max = 254)
    private String firstName;
    @Size(max = 254)
    private String lastName;
    @NonNull
    @Email
    @Size(max = 254)
    private String email;
    @NonNull
    @Size(min = 5, max = 254)
    private String password;
    @Size(max = 16)
    private String phone;
    @Size(max = 20)
    private UserRole role;

    public NewUserDto(String firstName, String lastName, @NonNull String email, @NonNull String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = UserRole.ROLE_USER;
    }
}
