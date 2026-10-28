package org.example.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.User.login.LoginRequest;
import org.example.User.login.LoginResponse;
import org.example.dtos.UserCreationDTO;
import org.example.dtos.UserViewDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public Long createUser(@Valid @RequestBody UserCreationDTO dto){
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserViewDTO getUser (@PathVariable Long id){
        return userService.getUser(id);
    }

    @GetMapping("/login")
    public LoginResponse login (@RequestBody LoginRequest request){
        return userService.login(request);
    }

}
