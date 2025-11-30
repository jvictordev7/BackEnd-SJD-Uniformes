package br.com.sjduniformes.controller;

import br.com.sjduniformes.dto.CaixaResumoDTO;
import br.com.sjduniformes.dto.CaixaSaidaRequest;
import br.com.sjduniformes.entity.CaixaLancamento;
import br.com.sjduniformes.service.CaixaLancamentoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaLancamentoService caixaLancamentoService;

    @GetMapping("/lancamentos")
    public ResponseEntity<List<CaixaLancamento>> listarLancamentos() {
        return ResponseEntity.ok(caixaLancamentoService.listarTodos());
    }

    @GetMapping("/resumo")
    public ResponseEntity<CaixaResumoDTO> obterResumo() {
        return ResponseEntity.ok(caixaLancamentoService.calcularResumo());
    }

    @PostMapping("/lancamentos/saida")
    public ResponseEntity<CaixaLancamento> lancarSaida(@RequestBody CaixaSaidaRequest request) {
        return ResponseEntity.ok(caixaLancamentoService.registrarSaida(request));
    }
}
