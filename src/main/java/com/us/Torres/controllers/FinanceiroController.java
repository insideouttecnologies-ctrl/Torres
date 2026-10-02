package com.us.Torres.controllers;

import com.us.Torres.models.Fatura;
import com.us.Torres.models.Pagamento;
import com.us.Torres.models.financeiro.FaturaRequest;
import com.us.Torres.models.financeiro.PagamentoRequest;
import com.us.Torres.service.FinanceiroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/financeiro", "/v1/financeiro"})
@RequiredArgsConstructor
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    @GetMapping("/faturas")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    public ResponseEntity<List<Fatura>> listarFaturas() {
        return ResponseEntity.ok(financeiroService.listarFaturas());
    }

    @GetMapping("/faturas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','MEDICO')")
    public ResponseEntity<Fatura> buscarFatura(@PathVariable String id) {
        return ResponseEntity.ok(financeiroService.buscarFatura(id));
    }

    @PostMapping("/faturas")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Fatura> criarFatura(@RequestBody FaturaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.criarFatura(request));
    }

    @GetMapping("/pagamentos")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<List<Pagamento>> listarPagamentos() {
        return ResponseEntity.ok(financeiroService.listarPagamentos());
    }

    @PostMapping("/faturas/{id}/pagamentos")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<Pagamento> processarPagamento(@PathVariable String id, @RequestBody PagamentoRequest request) {
        request.setFaturaId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroService.processarPagamento(request));
    }
}
