package com.us.Torres.repository;

import com.us.Torres.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCodigoFuncionario(String codigoFuncionario);
    List<User> findAllByOrderByNomeAsc();
    List<User> findByStatusOrderByCriadoEmDesc(User.StatusUsuario status);
}
