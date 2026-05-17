package dev.carlosmoises.projeto.enferm.controller;

import dev.carlosmoises.projeto.enferm.DTO.UpdateAvatarDTO;
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
                userLogged.getCoren(),
                userLogged.getName(),
                userLogged.getAvatar()
        );

        return ResponseEntity.ok(user);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal User userlogged, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        userService.updatePassword(userlogged, updatePasswordDTO);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/username")
    public ResponseEntity<ResponseMessage> updateUsername(@AuthenticationPrincipal User userlogged, @RequestBody UserResponseDTO userResponseDTO) {
        userService.updateName(userlogged, userResponseDTO);

        var response = new ResponseMessage("Nome alterado com sucesso");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/avatar")
    public ResponseEntity<ResponseMessage> updateAvatar(@AuthenticationPrincipal User userlogged, @RequestBody UpdateAvatarDTO updateAvatarDTO) {
        userService.updateAvatar(userlogged, updateAvatarDTO);

        var response = new ResponseMessage("Icone alterado com sucesso");

        return ResponseEntity.ok(response);
    }
}
