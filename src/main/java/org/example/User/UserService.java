package org.example.User;

import lombok.RequiredArgsConstructor;
import org.example.User.login.LoginRequest;
import org.example.User.login.LoginResponse;
import org.example.dtos.UserCreationDTO;
import org.example.dtos.UserViewDTO;
import org.example.utils.exceptions.EmailAlreadyExistsException;
import org.example.utils.exceptions.InvalidPassword;
import org.example.utils.exceptions.ResourceNotFoundException; // CHANGED: replaces the java.util.NoSuchElementException import
import org.example.utils.JwtService;
import org.example.utils.RefreshToken.RefreshTokenService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,64}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Long createUser(UserCreationDTO user) {

        String email = user.getEmail().toLowerCase().trim();
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email)))
            throw new EmailAlreadyExistsException(email);

        if (!isValidPassword(user.getPassword()))
            throw new InvalidPassword();

        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(email)
                .phoneNumber(user.getPhoneNumber())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(user.getRole())
                .build();

        try {
            return userRepository.save(newUser).getId();
        } catch (DataIntegrityViolationException e) {
            // two simultaneous requests: the unique constraint caught the second one
            throw new EmailAlreadyExistsException(newUser.getEmail());
        }
    }
    public User findById(Long id){
        // CHANGED: was NoSuchElementException (→ 500); ResourceNotFoundException is mapped to 404
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }
    public UserViewDTO getUser(Long id){
        return new UserViewDTO(findById(id));
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase().trim();

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword()));

        User user = (User) auth.getPrincipal();


        return new LoginResponse(user.getId(), user.getEmail(), user.getRole(), jwtService.generateAccessToken(user), refreshTokenService.createRefreshToken(user));
    }
}
