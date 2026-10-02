package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.UnidadeHospitalar;
import com.us.Torres.repository.UnidadeHospitalarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadeHospitalarService {

    private final UnidadeHospitalarRepository unidadeHospitalarRepository;

    public List<UnidadeHospitalar> listarTodos() {
        return unidadeHospitalarRepository.findAllByOrderByNomeFilialAsc();
    }

    public List<UnidadeHospitalar> listarAtivas() {
        return unidadeHospitalarRepository.findByAtivaTrueOrderByNomeFilialAsc();
    }

    public UnidadeHospitalar buscarPorId(String id) {
        return unidadeHospitalarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade hospitalar não encontrada."));
    }

    public UnidadeHospitalar criar(UnidadeHospitalar unidade) {
        validar(unidade);
        return unidadeHospitalarRepository.save(unidade);
    }

    public UnidadeHospitalar atualizar(String id, UnidadeHospitalar unidadeAtualizada) {
        UnidadeHospitalar unidade = buscarPorId(id);
        validar(unidadeAtualizada);

        unidade.setNomeFilial(unidadeAtualizada.getNomeFilial());
        unidade.setLocalizacao(unidadeAtualizada.getLocalizacao());
        unidade.setAtiva(unidadeAtualizada.isAtiva());

        return unidadeHospitalarRepository.save(unidade);
    }

    public UnidadeHospitalar alterarStatus(String id, boolean ativa) {
        UnidadeHospitalar unidade = buscarPorId(id);
        unidade.setAtiva(ativa);
        return unidadeHospitalarRepository.save(unidade);
    }

    private void validar(UnidadeHospitalar unidade) {
        if (unidade == null) {
            throw new BusinessException("Dados da unidade hospitalar são obrigatórios.");
        }
        if (unidade.getNomeFilial() == null || unidade.getNomeFilial().isBlank()) {
            throw new BusinessException("Nome da filial é obrigatório.");
        }
    }
}
