package com.us.Torres.repository;

import com.us.Torres.models.Funcionario;
import com.us.Torres.models.users.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, String> {
    List<Funcionario> findAllByOrderByNomeAsc();
    List<Funcionario> findByCargoOrderByNomeAsc(Cargo cargo);
    Optional<Funcionario> findByCodigoFuncionario(String codigoFuncionario);
    Optional<Funcionario> findByEmail(String email);
    boolean existsByCodigoFuncionario(String codigoFuncionario);
    boolean existsByEmail(String email);
}
