package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.model.User;
import com.example.examenfabricadegauss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Método para crear un nuevo usuario
    public User createUser(String name, String email) {
        User newUser = new User(name, email);
        return userRepository.save(newUser);
    }

    // Método para buscar un usuario por ID
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Método para actualizar un usuario existente
    public User updateUser(Long id, String name, String email) {
        User user = findUserById(id);
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    // Método para eliminar un usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
