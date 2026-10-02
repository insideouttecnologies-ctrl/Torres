package com.us.Torres.controllers;

import com.us.Torres.models.Notificacao;
import com.us.Torres.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1", "/v1"})
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping("/notificacoes/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO')")
    public ResponseEntity<List<Notificacao>> listarPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/notificacoes/usuario/{usuarioId}/nao-lidas")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO')")
    public ResponseEntity<List<Notificacao>> listarNaoLidas(@PathVariable String usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidas(usuarioId));
    }

    @PostMapping("/notificacoes")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO')")
    public ResponseEntity<Notificacao> criar(@RequestBody Notificacao notificacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacaoService.criar(notificacao));
    }

    @PatchMapping("/notificacoes/{id}/ler")
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO','ENFERMEIRO','FARMACEUTICO','RECEPCIONISTA','TECNICO')")
    public ResponseEntity<Notificacao> marcarComoLida(@PathVariable String id) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id));
    }
}
