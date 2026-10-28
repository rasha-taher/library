package org.example.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.User.RoleEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {
    private String firstName;
    private String lastName;
    @NotBlank
    private String email;
    private String password;
    private RoleEnum role;
    private String phoneNumber;

}
