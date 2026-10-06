package com.us.Torres.controllers;

import com.us.Torres.models.users.StatusUpdateRequest;
import com.us.Torres.models.users.User;
import com.us.Torres.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/users", "/v1/users"})
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listarUsuarios() {
        return ResponseEntity.ok(userRepository.findAllByOrderByNomeAsc());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> listarPendentes() {
        return ResponseEntity.ok(userRepository.findByStatusOrderByCriadoEmDesc(User.StatusUsuario.PENDENTE));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> buscarUsuario(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> atualizarStatus(@PathVariable String id, @RequestBody StatusUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (request == null || request.status() == null) {
            return ResponseEntity.badRequest().build();
        }

        user.setStatus(request.status());
        return ResponseEntity.ok(userRepository.save(user));
    }
}
