package com.ikhzan.shm.web;

import com.ikhzan.shm.data.User;
import com.ikhzan.shm.exceptions.UserNotFoundException;
import com.ikhzan.shm.services.UserService;
import com.ikhzan.shm.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * this is authenticated page that needs authentication
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(BCryptPasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Check if the username already exists
            if (userService.findByName(user.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            // Encode the password before saving
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUserData(user);
            return ResponseEntity.ok("User registered successfully");
        }catch (Exception ex){
            // Log the exception for debugging
            showWarnLog("registerUser", ex);
            return ResponseEntity.badRequest().body("An error occurred during r");
        }

    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            // Fetch the user from database
            User user = userService.findByName(username);

            // Verify user existence and password match
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                String token = JwtUtil.generateToken(username);
                // Success response
                return ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "username", username,
                        "token",token,
                        "role",user.getRole()
                ));
            } else {
                // Unauthorized response
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "error", "Invalid username or password"
                ));
            }
        } catch (Exception ex) {
            // Log exception and return internal server error response
            showWarnLog("loginUser", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An unexpected error occurred"
            ));
        }

    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        try {
            // Perform the update operation
            User updatedEntity = userService.updateUser(id, updatedUser);

            // Return success response
            return ResponseEntity.ok(updatedEntity);
        } catch (UserNotFoundException ex) {
            // Handle user not found scenario
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "User not found",
                    "id", id
            ));
        } catch (Exception ex) {
            // Log and return internal server error for unexpected exceptions
            showWarnLog("updateUser",ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An unexpected error occurred"
            ));
        }
    }

    private void showWarnLog(String method,Exception ex){
        logger.warn("Error {}", ex.getMessage());
    }

}
