package com.us.Torres.repository;

import com.us.Torres.models.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, String> {
    List<Notificacao> findByUsuarioDestinoIdOrderByDataCriacaoDesc(String usuarioDestinoId);
    List<Notificacao> findByUsuarioDestinoIdAndStatusOrderByDataCriacaoDesc(String usuarioDestinoId, Notificacao.StatusNotificacao status);
}
