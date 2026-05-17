package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.DTO.CreateUserDTO;
import dev.carlosmoises.projeto.enferm.DTO.UpdateAvatarDTO;
import dev.carlosmoises.projeto.enferm.DTO.UpdatePasswordDTO;
import dev.carlosmoises.projeto.enferm.DTO.UserResponseDTO;
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

        var user = new User(null, createUserDTO.email(), createUserDTO.coren(), hashPassword, createUserDTO.name(), "anonimo.png");

        var userSaved = userRepository.save(user);

        return userSaved.getId();
    }

    public Long updatePassword(User user, UpdatePasswordDTO updatePasswordDTO) {
        var passwordUser = user.getPassword();
        var currentPassword = updatePasswordDTO.currentPassword();
        var newPassword = updatePasswordDTO.newPassword();

        if (!encoder.matches(currentPassword, passwordUser)) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }

        var newHash = encoder.encode(newPassword);

        user.setPassword(newHash);

        var userSaved = userRepository.save(user);

        return userSaved.getId();
    }

    public String updateName(User user, UserResponseDTO userResponseDTO) {
        var newName = userResponseDTO.name();

        user.setName(newName);

        var nameSaved = userRepository.save(user);

        return nameSaved.getName();
    }

    public String updateAvatar(User user, UpdateAvatarDTO updateAvatarDTO) {
        var newIcon = updateAvatarDTO.avatar();

        user.setAvatar(newIcon);

        var iconSaved = userRepository.save(user);

        return iconSaved.getAvatar();
    }
}
