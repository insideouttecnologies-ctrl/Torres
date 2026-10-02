package com.us.Torres.service;

import com.us.Torres.infra.exceptions.BusinessException;
import com.us.Torres.infra.exceptions.ResourceNotFoundException;
import com.us.Torres.models.Notificacao;
import com.us.Torres.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public List<Notificacao> listarPorUsuario(String usuarioDestinoId) {
        return notificacaoRepository.findByUsuarioDestinoIdOrderByDataCriacaoDesc(usuarioDestinoId);
    }

    public List<Notificacao> listarNaoLidas(String usuarioDestinoId) {
        return notificacaoRepository.findByUsuarioDestinoIdAndStatusOrderByDataCriacaoDesc(
                usuarioDestinoId,
                Notificacao.StatusNotificacao.NAO_LIDA
        );
    }

    public Notificacao criar(Notificacao notificacao) {
        validar(notificacao);
        return notificacaoRepository.save(notificacao);
    }

    public Notificacao marcarComoLida(String id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação não encontrada."));
        notificacao.setStatus(Notificacao.StatusNotificacao.LIDA);
        return notificacaoRepository.save(notificacao);
    }

    private void validar(Notificacao notificacao) {
        if (notificacao == null) {
            throw new BusinessException("Dados da notificação são obrigatórios.");
        }
        if (notificacao.getUsuarioDestinoId() == null || notificacao.getUsuarioDestinoId().isBlank()) {
            throw new BusinessException("Usuário destinatário é obrigatório.");
        }
        if (notificacao.getTitulo() == null || notificacao.getTitulo().isBlank()) {
            throw new BusinessException("Título da notificação é obrigatório.");
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().isBlank()) {
            throw new BusinessException("Mensagem da notificação é obrigatória.");
        }
    }
}
