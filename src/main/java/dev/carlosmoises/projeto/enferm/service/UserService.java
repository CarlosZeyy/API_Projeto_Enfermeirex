package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.CreateUserDTO;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public Long createUser(CreateUserDTO createUserDTO) {
        var hashPassword = encoder.encode(createUserDTO.password());

        if (userRepository.findByEmailOrCoren(createUserDTO.email(), createUserDTO.coren()) != null) {
            throw new IllegalArgumentException("Email/COREN já cadastrado.");
        }


        var user = new User(null, createUserDTO.email(), createUserDTO.coren(), hashPassword);

        var userSaved = userRepository.save(user);

        return userSaved.getId();
    }
}
