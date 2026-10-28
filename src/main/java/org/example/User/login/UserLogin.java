package org.example.User.login;

import lombok.RequiredArgsConstructor;
import org.example.User.User;
import org.example.User.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLogin implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username==null)             throw new UsernameNotFoundException("Invalid email or password");

        String email = username.toLowerCase().trim();
        return userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Invalid email or password "));

    }

}
