package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.UpdatePasswordDTO;
import dev.carlosmoises.projeto.enferm.DTO.UserResponseDTO;
import dev.carlosmoises.projeto.enferm.model.User;
import dev.carlosmoises.projeto.enferm.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserProfile(@AuthenticationPrincipal User userLogged) {
        var user = new UserResponseDTO(
                userLogged.getId(),
                userLogged.getEmail(),
                userLogged.getCoren()
        );

        return ResponseEntity.ok(user);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> resetPassword(@AuthenticationPrincipal User userlogged, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        userService.resetPassword(userlogged, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

}
