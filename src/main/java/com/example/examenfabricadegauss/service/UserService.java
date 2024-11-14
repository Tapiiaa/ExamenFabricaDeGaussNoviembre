package com.example.examenfabricadegauss.service;

import com.example.examenfabricadegauss.exception.UserNotFoundException;
import com.example.examenfabricadegauss.model.AppUser;
import com.example.examenfabricadegauss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Método para crear un nuevo usuario
    public AppUser createUser(String name, String email) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("El nombre o el email no pueden ser nulos o vacios");
        }
        AppUser newUser = new AppUser(name, email);
        return userRepository.save(newUser);
    }

    // Método para buscar un usuario por ID
    public AppUser findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Método para actualizar un usuario existente
    public AppUser updateUser(Long id, String name, String email) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        // Validación de datos
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    // Método para eliminar un usuario
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}
