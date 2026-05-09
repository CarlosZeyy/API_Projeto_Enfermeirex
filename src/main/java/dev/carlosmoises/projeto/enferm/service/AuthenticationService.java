package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.model.PasswordResetToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.PasswordResetTokenRepository;
import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    AuthenticationService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userExists = userRepository.findByEmailOrCoren(username, username);
        if (userExists != null) {
            return userExists;
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

    }

    public void requestPasswordReset(String identification) {
        var userExists = userRepository.findOptionalByEmailOrCoren(identification, identification);

        if (userExists.isEmpty()) {
            return;
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken ticket = new PasswordResetToken();

        User userFound = userExists.get();

        ticket.setToken(token);
        ticket.setUser(userFound);
        ticket.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(ticket);

        emailService.sendPasswordResetEmail(userFound.getEmail(), "Recuperação de Senha", "Acesse o link para redefinir: http://localhost:5173/reset-password?token=" + token);
    }
}
