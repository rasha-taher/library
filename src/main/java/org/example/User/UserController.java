package org.example.User;

import lombok.RequiredArgsConstructor;
import org.example.dtos.UserViewDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserViewDTO getUser (@PathVariable Long id){
        return userService.getUser(id);
    }

}
