package com.ikhzan.shm.configs;

import com.ikhzan.shm.data.User;
import com.ikhzan.shm.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserInitConfig {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Dotenv dotenv = Dotenv.load();

    public UserInitConfig(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initUsers(){
        if (userRepository.findByUsername(dotenv.get("USER_NAME1")) == null){
            // create admin user
            User user = new User(dotenv.get("USER_NAME1"),passwordEncoder.encode(dotenv.get("PASS1")));
            user.setRole(dotenv.get("ROLE1"));
            userRepository.save(user);
        }

        if (userRepository.findByUsername(dotenv.get("USER_NAME2")) == null){
            // create admin user
            User user = new User(dotenv.get("USER_NAME2"),passwordEncoder.encode(dotenv.get("PASS2")));
            user.setRole(dotenv.get("ROLE2"));
            userRepository.save(user);
        }
    }
}
