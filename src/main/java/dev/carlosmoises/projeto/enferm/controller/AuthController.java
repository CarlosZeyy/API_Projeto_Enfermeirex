package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.*;
import dev.carlosmoises.projeto.enferm.model.RefreshToken;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.RefreshTokenRepository;
import dev.carlosmoises.projeto.enferm.service.AuthenticationService;
import dev.carlosmoises.projeto.enferm.service.TokenService;
import dev.carlosmoises.projeto.enferm.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> createToken(@Valid @RequestBody AuthDTO authDTO) {
        var authTicket = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());

        var auth = authenticationManager.authenticate(authTicket);

        var user = (User) auth.getPrincipal();

        var accessToken = tokenService.generateToken(user);
        var refreshToken = tokenService.createRefreshToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) throws MessagingException {
        String responseMessage = authenticationService.requestPasswordReset(forgotPasswordDTO.identification());

        return ResponseEntity.ok(new ResponseMessage(responseMessage));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        authenticationService.resetPassword(resetPasswordDTO.token(), resetPasswordDTO.newPassword());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO tokenRequestDTO) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(tokenRequestDTO.refreshToken()).orElseThrow(() -> new RuntimeException("Refresh Token não encontrado ou inválido no banco de dados!"));

        tokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        String newToken = tokenService.generateToken(user);

        refreshTokenRepository.delete(refreshToken);

        var newRefreshToken = tokenService.createRefreshToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(newToken, newRefreshToken.getToken()));
    }
}
