package br.com.sjduniformes.dto.pedido;

import br.com.sjduniformes.enums.EtapaProducao;
import br.com.sjduniformes.enums.StatusPagamento;
import br.com.sjduniformes.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    private Long clienteId;
    private LocalDate dataEntrega;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;
    private String observacoesGerais;
    private String logoEmpresaUrl;
    private String imagemReferenciaUrl;
    private List<PedidoItemRequestDTO> itens;
    private StatusPedido statusPedido;
    private StatusPagamento statusPagamento;
    private EtapaProducao etapaProducao;
}
