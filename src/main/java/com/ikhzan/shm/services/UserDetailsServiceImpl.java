package com.ikhzan.shm.services;

import com.ikhzan.shm.data.User;
import com.ikhzan.shm.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.repository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("load user by username {}", username);
        User user = repository.findByUsername(username);
        if (user == null){
            logger.warn("user {} NOT FOUND", username);
            throw new UsernameNotFoundException("user not found");
        }else{
            logger.info("user found {}", username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), new ArrayList<>());
    }
}
