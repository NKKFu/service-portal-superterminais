package com.st.portal.controller;

import com.st.portal.model.User;
import com.st.portal.payload.request.LoginRequest;
import com.st.portal.payload.request.SignupRequest;
import com.st.portal.payload.response.JwtResponse;
import com.st.portal.payload.response.MessageResponse;
import com.st.portal.repository.UserRepository;
import com.st.portal.security.JwtUtils;
import com.st.portal.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @Value("${app.registration.secret:secret-allow-first-user}")
    private String registrationSecret;

    @Value("${app.jwt.expirationMs}")
    private int jwtExpirationMs;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
            PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            ResponseCookie jwtCookie = ResponseCookie.from("st-service-token", jwt)
                    .path("/")
                    .maxAge(jwtExpirationMs / 1000)
                    .httpOnly(false)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("wrong username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest, HttpServletRequest request) {
        boolean authorized = false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            if (userDetails.isInternal()) {
                authorized = true;
            }
        }

        if (!authorized) {
            String headerAuth = request.getHeader("Authorization");
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                String token = headerAuth.substring(7);
                if (registrationSecret.equals(token)) {
                    authorized = true;
                }
            }
        }

        if (!authorized) {
            return ResponseEntity.badRequest().body(new MessageResponse("Unauthorized to register users. Must be an internal user or provide the registration secret."));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.isInternal());

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
