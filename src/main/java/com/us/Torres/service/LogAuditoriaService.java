package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.models.LogAuditoria;
import com.us.Torres.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogAuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public List<LogAuditoria> listarTodos() {
        return logAuditoriaRepository.findAllByOrderByDataHoraDesc();
    }

    public List<LogAuditoria> listarPorUsuario(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            throw new BusinessException("Usuário é obrigatório para consultar o log de auditoria.");
        }
        return logAuditoriaRepository.findByUsuarioIdOrderByDataHoraDesc(usuarioId);
    }

    public LogAuditoria registrar(String usuarioId, String acao, String modulo, String ipOrigem, String detalhes) {
        LogAuditoria log = LogAuditoria.builder()
                .usuarioId(usuarioId)
                .acao(acao)
                .modulo(modulo)
                .ipOrigem(ipOrigem)
                .detalhes(detalhes)
                .build();

        validar(log);
        return logAuditoriaRepository.save(log);
    }

    private void validar(LogAuditoria log) {
        if (log == null) {
            throw new BusinessException("Dados do log de auditoria são obrigatórios.");
        }
        if (log.getAcao() == null || log.getAcao().isBlank()) {
            throw new BusinessException("Ação da auditoria é obrigatória.");
        }
        if (log.getModulo() == null || log.getModulo().isBlank()) {
            throw new BusinessException("Módulo da auditoria é obrigatório.");
        }
    }
}
