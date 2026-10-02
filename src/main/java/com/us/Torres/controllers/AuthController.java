package com.us.Torres.controllers;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ConflictException;
import com.us.Torres.models.users.*;
import com.us.Torres.repository.UserRepository;
import com.us.Torres.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/v1/auth", "/v1/auth"})
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody AuthRecord login) {
        if (login == null || login.email() == null || login.password() == null) {
            throw new BusinessException("Email e senha são obrigatórios.");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(login.email(), login.password());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var user = (User) authentication.getPrincipal();
        var token = tokenService.generateToken(user);

        var response = new ResponseDTO(
                token,
                "Autenticação realizada com sucesso",
                new UsuarioResponse(
                        user.getId(),
                        user.getNome(),
                        user.getEmail(),
                        user.getCargo(),
                        user.getCodigoFuncionario(),
                        user.getStatus()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> obterUsuario(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = repository.findByEmail(authentication.getName()).orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UsuarioResponse response = new UsuarioResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getCargo(),
                user.getCodigoFuncionario(),
                user.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        if (registerDTO == null || registerDTO.email() == null || registerDTO.password() == null || registerDTO.name() == null) {
            throw new BusinessException("Nome, email, senha e cargo são obrigatórios.");
        }

        if (registerDTO.password().length() < 6) {
            throw new BusinessException("A senha deve ter ao menos 6 caracteres.");
        }

        if (repository.findByEmail(registerDTO.email()).isPresent()) {
            throw new ConflictException("Email já cadastrado no sistema.");
        }

        if (repository.findByCodigoFuncionario(registerDTO.codigoFuncionario()).isPresent()) {
            throw new ConflictException("Código de funcionário já cadastrado no sistema.");
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());

        var user = new User(
                registerDTO.name(),
                registerDTO.email(),
                encryptedPassword,
                registerDTO.cargo(),
                registerDTO.codigoFuncionario()
        );

        repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                "Solicitação de cadastro enviada com sucesso! O acesso será validado pela coordenação."
        );
    }
}