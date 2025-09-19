package org.ean.techlogistics.controller;

import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.UserDTO;
import org.ean.techlogistics.entity.User;
import org.ean.techlogistics.mapper.EntityMapper;
import org.ean.techlogistics.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO dto, @RequestParam String password) {
        User user = EntityMapper.toUserEntity(dto);
        User saved = userService.registerUser(user, password);
        return ResponseEntity.ok(EntityMapper.toUserDTO(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password)
                .map(u -> ResponseEntity.ok(EntityMapper.toUserDTO(u)))
                .orElse(ResponseEntity.status(401).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        User updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(EntityMapper.toUserDTO(updated));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestParam String newPassword) {
        userService.changePassword(id, newPassword);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/couriers")
    public List<UserDTO> getCouriers() {
        return userService.getCouriers().stream().map(EntityMapper::toUserDTO).toList();
    }

    @GetMapping("")
    public List<UserDTO> getAll() {
        return userService.getAllUsers().stream().map(EntityMapper::toUserDTO).toList();
    }
}
