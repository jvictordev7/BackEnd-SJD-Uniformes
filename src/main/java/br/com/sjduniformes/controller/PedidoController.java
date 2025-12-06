package br.com.sjduniformes.controller;

import br.com.sjduniformes.dto.pedido.AlterarEtapaRequestDTO;
import br.com.sjduniformes.dto.pedido.PedidoRequestDTO;
import br.com.sjduniformes.dto.pedido.PedidoResponseDTO;
import br.com.sjduniformes.dto.pedido.RegistroPagamentoDTO;
import br.com.sjduniformes.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO criado = pedidoService.criarPedido(request);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista de todos os pedidos")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@Parameter(description = "ID do pedido") @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PutMapping("/{id}/etapa")
    @Operation(summary = "Alterar etapa do pedido", description = "Atualiza a etapa de produção de um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Etapa alterada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> alterarEtapa(@Parameter(description = "ID do pedido") @PathVariable Long id, @RequestBody AlterarEtapaRequestDTO request) {
        return ResponseEntity.ok(pedidoService.alterarEtapa(id, request.getEtapaProducao()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza um pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> atualizar(@Parameter(description = "ID do pedido") @PathVariable Long id, @RequestBody PedidoRequestDTO request) {
        return ResponseEntity.ok(pedidoService.atualizarPedido(id, request));
    }

    @PutMapping("/{id}/pagamento")
    @Operation(summary = "Registrar pagamento", description = "Registra um pagamento para um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoResponseDTO> registrarPagamento(@Parameter(description = "ID do pedido") @PathVariable Long id, @RequestBody RegistroPagamentoDTO pagamento) {
        return ResponseEntity.ok(pedidoService.registrarPagamento(id, pagamento));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido", description = "Remove um pedido do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do pedido") @PathVariable Long id) {
        pedidoService.excluirPedido(id);
        return ResponseEntity.noContent().build();
    }
}
