package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.AuthDTO;
import dev.carlosmoises.projeto.enferm.DTO.CreateUserDTO;
import dev.carlosmoises.projeto.enferm.DTO.TokenResponseDTO;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.repository.UserRepository;
import dev.carlosmoises.projeto.enferm.service.TokenService;
import dev.carlosmoises.projeto.enferm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> createToken(@Valid @RequestBody AuthDTO authDTO) {
        var authTicket = new UsernamePasswordAuthenticationToken(authDTO.login(), authDTO.password());

        var auth = authenticationManager.authenticate(authTicket);

        var user = (User) auth.getPrincipal();

        var tokenSaved = tokenService.generateToken(user);

        return ResponseEntity.ok(new TokenResponseDTO(tokenSaved));
    }

    @PostMapping("/register")
    public ResponseEntity<CreateUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);

        return ResponseEntity.ok().build();
    }
}
