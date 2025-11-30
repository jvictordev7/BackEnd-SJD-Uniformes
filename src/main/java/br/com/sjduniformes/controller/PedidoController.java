package br.com.sjduniformes.controller;

import br.com.sjduniformes.dto.pedido.AlterarEtapaRequestDTO;
import br.com.sjduniformes.dto.pedido.PedidoRequestDTO;
import br.com.sjduniformes.dto.pedido.PedidoResponseDTO;
import br.com.sjduniformes.dto.pedido.RegistroPagamentoDTO;
import br.com.sjduniformes.service.PedidoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@RequestBody PedidoRequestDTO request) {
        PedidoResponseDTO criado = pedidoService.criarPedido(request);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PutMapping("/{id}/etapa")
    public ResponseEntity<PedidoResponseDTO> alterarEtapa(@PathVariable Long id, @RequestBody AlterarEtapaRequestDTO request) {
        return ResponseEntity.ok(pedidoService.alterarEtapa(id, request.getEtapaProducao()));
    }

    @PutMapping("/{id}/pagamento")
    public ResponseEntity<PedidoResponseDTO> registrarPagamento(@PathVariable Long id, @RequestBody RegistroPagamentoDTO pagamento) {
        return ResponseEntity.ok(pedidoService.registrarPagamento(id, pagamento));
    }
}
