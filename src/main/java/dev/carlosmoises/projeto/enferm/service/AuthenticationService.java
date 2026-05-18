package dev.carlosmoises.projeto.enferm.service;

import dev.carlosmoises.projeto.enferm.model.PasswordResetToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.PasswordResetTokenRepository;
import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${URL}")
    private String frontendUrl;

    public String requestPasswordReset(String identification) throws MessagingException {
        var userExists = userRepository.findOptionalByEmailOrCoren(identification, identification);

        if (userExists.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        String token = UUID.randomUUID().toString();

        PasswordResetToken ticket = new PasswordResetToken();

        User userFound = userExists.get();

        String emailUser = userFound.getEmail();
        var atIndex = emailUser.indexOf("@");
        var afterAt = emailUser.substring(atIndex);
        var initialEmail = emailUser.substring(0, 2);
        var endEmail = emailUser.substring(atIndex - 1, atIndex);
        var hiddenEmail = initialEmail + "***" + endEmail + afterAt;

        String finalMessage = "";

        if (atIndex <= 2) {
            var firstLetter = emailUser.substring(0, 1);
            var hiddenShortEmail = firstLetter + "***" + afterAt;
            finalMessage = "Verifique o e-mail " + hiddenShortEmail + " para recuperar sua senha:";
        } else {
            finalMessage = "Verifique o e-mail " + hiddenEmail + " para recuperar sua senha:";
        }

        ticket.setToken(token);
        ticket.setUser(userFound);
        ticket.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        passwordResetTokenRepository.save(ticket);

        String linkComplete = frontendUrl + "/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(userFound.getEmail(), "Recuperação de Senha", linkComplete, userFound.getName());

        return finalMessage;
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
