package com.us.Torres.repository;

import com.us.Torres.models.UnidadeHospitalar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeHospitalarRepository extends JpaRepository<UnidadeHospitalar, String> {

    List<UnidadeHospitalar> findAllByOrderByNomeFilialAsc();

    List<UnidadeHospitalar> findByAtivaTrueOrderByNomeFilialAsc();
}
