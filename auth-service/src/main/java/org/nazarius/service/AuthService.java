package org.nazarius.service;

import org.mindrot.jbcrypt.BCrypt;
import org.nazarius.dto.AuthDto;
import org.nazarius.model.User;

import java.util.List;

public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Authenticate an existing user or create a new one if not exists.
     * Returns a JWT access token.
     */
    public String authenticate(String username, String password) {
        User user = userService.getEntityByUsername(username);

        if (user != null) {
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new IllegalArgumentException("Invalid credentials for user: " + username);
            }
        } else {
            AuthDto newUser = new AuthDto();
            newUser.setUsername(username);
            newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            newUser.setRole("VIEWER");

            user = userService.getEntityByUsername(
                    userService.create(newUser).getUsername()
            );
        }

        List<String> roles = List.of(user.getRole());
        return jwtService.generateToken(user.getUsername(), roles);
    }
}