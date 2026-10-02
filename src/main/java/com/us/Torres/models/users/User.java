package com.us.Torres.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String nome;

    @Column(unique = true, nullable = false)
    @NotNull
    @NotBlank
    @NotEmpty
    private String email;

    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private Cargo cargo;

    @NotNull
    @NotEmpty
    @Column(name = "codigo_funcionario")
    private String codigoFuncionario;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private StatusUsuario status;

    private LocalDateTime ultimoAcesso;

    private LocalDateTime criadoEm;

    public enum StatusUsuario {
        PENDENTE,
        ATIVO,
        BLOQUEADO
    }

    public User(String nome, String email, String password, Cargo cargo, String codigoFuncionario) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.cargo = cargo;
        this.status = StatusUsuario.PENDENTE;
        this.codigoFuncionario = codigoFuncionario;
        this.criadoEm = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = StatusUsuario.PENDENTE;
        }
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.cargo == null) {
            return List.of();
        }
        return switch (this.cargo) {
            case ADMIN -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
            case ENFERMEIRO -> List.of(new SimpleGrantedAuthority("ROLE_ENFERMEIRO"));
            case FARMACEUTICO -> List.of(new SimpleGrantedAuthority("ROLE_FARMACEUTICO"));
            case MEDICO -> List.of(new SimpleGrantedAuthority("ROLE_MEDICO"));
            case TECNICO -> List.of(new SimpleGrantedAuthority("ROLE_TECNICO"));
            case LABORATORISTA -> List.of(
                    new SimpleGrantedAuthority("ROLE_LABORATORISTA"),
                    new SimpleGrantedAuthority("ROLE_TECNICO")
            );
            case RECEPCIONISTA -> List.of(new SimpleGrantedAuthority("ROLE_RECEPCIONISTA"));
        };
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != StatusUsuario.BLOQUEADO;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == StatusUsuario.ATIVO;
    }
}
