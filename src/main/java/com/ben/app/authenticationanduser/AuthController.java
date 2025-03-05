package com.ben.app.authenticationanduser;


import com.ben.app.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", "Email already in use"));
        }

        UserModel userModel = new UserModel();
        userModel.setName(registerRequestDTO.getName());
        userModel.setEmail(registerRequestDTO.getEmail());
        userModel.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        // Assign an admin role by default // This is for first instance when someone wants to access the POS
        //userModel.setRole(Role.ADMIN);

        //userModel.setStatus(ProfileStatus.INACTIVE);


        UserModel savedUser = userRepository.save(userModel);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "status", "success",
                        "message", "User registered successfully",
                        "user", Map.of(
                                "id", savedUser.getId(),
                                "name", savedUser.getName(),
                                "email", savedUser.getEmail()
                        )
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<UserModel> existingUser = userRepository.findByEmail(loginRequestDTO.getEmail());
        if (existingUser.isEmpty() || !passwordEncoder.matches(loginRequestDTO.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", "Invalid email or password"));
        }

        String token = jwtUtil.generateToken(existingUser.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(
                        "status", "success",
                        "message", "Login successful",
                        "token", token,
                        "user", Map.of(
                                "id", existingUser.get().getId(),
                                "name", existingUser.get().getName(),
                                "email", existingUser.get().getEmail()

                        )
                ));
    }
}
