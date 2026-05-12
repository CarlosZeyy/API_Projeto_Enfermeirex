package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.model.PasswordResetToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.PasswordResetTokenRepository;
import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    AuthenticationService(UserRepository userRepository, PasswordResetTokenRepository passwordResetTokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
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

    public String requestPasswordReset(String identification) {
        var userExists = userRepository.findOptionalByEmailOrCoren(identification, identification);

        if (userExists.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken ticket = new PasswordResetToken();

        User userFound = userExists.get();

        String emailUser = userFound.getEmail();
        var hiddenEmail = emailUser.substring(1).indexOf("@");

        ticket.setToken(token);
        ticket.setUser(userFound);
        ticket.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(ticket);

        emailService.sendPasswordResetEmail(userFound.getEmail(), "Recuperação de Senha", "Acesse o link para redefinir: http://localhost:5173/reset-password?token=" + token);

        return "Verifique o e-mail " + hiddenEmail + " para recuperar sua senha:";
    }

    public void resetPassword(String token, String newPassword) {
        var optionalTicket = passwordResetTokenRepository.findByToken(token);

        if (optionalTicket.isEmpty()) {
            throw new IllegalArgumentException("Token inválido ou não encontrado");
        }

        PasswordResetToken ticket = optionalTicket.get();

        if (ticket.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este link de recuperação já expirou");
        }

        User user = ticket.getUser();

        String passwordHash = passwordEncoder.encode(newPassword);

        user.setPassword(passwordHash);

        userRepository.save(user);

        passwordResetTokenRepository.delete(ticket);
    }
}
