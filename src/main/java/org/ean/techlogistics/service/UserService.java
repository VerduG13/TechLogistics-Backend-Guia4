package org.ean.techlogistics.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ean.techlogistics.dto.UserDTO;
import org.ean.techlogistics.entity.User;
import org.ean.techlogistics.entity.UserRole;
import org.ean.techlogistics.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User registerUser(User user, String rawPassword) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        user.setPassword(rawPassword);
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(rawPassword));
    }

    public List<User> getCouriers() {
        return userRepository.findByRole(UserRole.TRANSPORTISTA);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, UserDTO dto) {
        User existing = findById(id);
        if (dto.phoneNumber() != null) {
            existing.setPhoneNumber(dto.phoneNumber());
        }
        if (dto.name() != null) {
            existing.setName(dto.name());
        }
        return userRepository.save(existing);
    }

    @Transactional
    public void changePassword(Long id, String rawNewPassword) {
        User u = findById(id);
        u.setPassword(rawNewPassword);
        userRepository.save(u);
    }
}
