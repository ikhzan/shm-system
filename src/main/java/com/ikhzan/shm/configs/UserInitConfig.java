package com.ikhzan.shm.configs;

import com.ikhzan.shm.data.User;
import com.ikhzan.shm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserInitConfig {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserInitConfig(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers(){
        if (userRepository.findByUsername("admin") == null){
            // create admin user
            User user = new User("admin",passwordEncoder.encode("password"));
            user.setRole("ADMIN");
            userRepository.save(user);
        }

        if (userRepository.findByUsername("isan") == null){
            // create admin user
            User user = new User("isan",passwordEncoder.encode("bismillah"));
            user.setRole("USER");
            userRepository.save(user);
        }
    }
}
