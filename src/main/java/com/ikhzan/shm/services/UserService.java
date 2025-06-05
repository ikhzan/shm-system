package com.ikhzan.shm.services;

import com.ikhzan.shm.data.User;
import com.ikhzan.shm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User saveUserData(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUserData(){
        return userRepository.findAll();
    }

    public User getUserDataById(String id){
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserData(String id){
        userRepository.deleteById(id);
    }

    public User findByName(String name) {
        return userRepository.findByUsername(name);
    }

    public User updateUser(String id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

}
