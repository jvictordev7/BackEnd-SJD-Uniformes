package br.com.sjduniformes.dto.pedido;

import br.com.sjduniformes.enums.EtapaProducao;
import br.com.sjduniformes.enums.StatusPagamento;
import br.com.sjduniformes.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {
    private String numeroPedido;
    private Long pedidoId;
    private Long clienteId;
    private String nomeCliente;
    private LocalDateTime dataCriacao;
    private LocalDate dataEntrega;
    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;
    private StatusPedido statusPedido;
    private StatusPagamento statusPagamento;
    private EtapaProducao etapaProducao;
    private String observacoesGerais;
    private String logoEmpresaUrl;
    private String imagemReferenciaUrl;
    private List<PedidoItemResponseDTO> itens;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PedidoItemResponseDTO {
        private Long produtoId;
        private String nomeProduto;
        private Integer quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal valorTotalItem;
        private String tamanho;
        private String corTecido;
        private String tipoServico;
        private String detalhesPersonalizacao;
    }
}
