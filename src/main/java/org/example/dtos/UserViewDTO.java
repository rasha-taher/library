package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.User.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserViewDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public UserViewDTO(User user){
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
