package br.com.sjduniformes.controller;

import br.com.sjduniformes.dto.CaixaResumoDTO;
import br.com.sjduniformes.dto.CaixaSaidaRequest;
import br.com.sjduniformes.entity.CaixaLancamento;
import br.com.sjduniformes.service.CaixaLancamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Caixa", description = "Endpoints para gerenciamento do caixa e lançamentos")
public class CaixaController {

    private final CaixaLancamentoService caixaLancamentoService;

    @GetMapping("/lancamentos")
    @Operation(summary = "Listar lançamentos do caixa", description = "Retorna todos os lançamentos do caixa")
    @ApiResponse(responseCode = "200", description = "Lista de lançamentos retornada com sucesso")
    public ResponseEntity<List<CaixaLancamento>> listarLancamentos() {
        return ResponseEntity.ok(caixaLancamentoService.listarTodos());
    }

    @GetMapping("/resumo")
    @Operation(summary = "Obter resumo do caixa", description = "Retorna um resumo financeiro do caixa")
    @ApiResponse(responseCode = "200", description = "Resumo do caixa retornado com sucesso")
    public ResponseEntity<CaixaResumoDTO> obterResumo() {
        return ResponseEntity.ok(caixaLancamentoService.calcularResumo());
    }

    @PostMapping("/saida")
    @Operation(summary = "Lançar saída (atalho)", description = "Registra uma saída no caixa de forma simplificada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saída lançada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CaixaLancamento> lancarSaidaAtalho(@RequestBody CaixaSaidaRequest request) {
        return ResponseEntity.ok(caixaLancamentoService.registrarSaida(request));
    }

    @PostMapping("/lancamentos/saida")
    @Operation(summary = "Lançar saída completa", description = "Registra uma saída completa no caixa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saída lançada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CaixaLancamento> lancarSaida(@RequestBody CaixaSaidaRequest request) {
        return ResponseEntity.ok(caixaLancamentoService.registrarSaida(request));
    }
}
