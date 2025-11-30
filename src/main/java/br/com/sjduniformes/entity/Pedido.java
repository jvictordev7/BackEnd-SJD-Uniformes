package br.com.sjduniformes.entity;

import br.com.sjduniformes.enums.EtapaProducao;
import br.com.sjduniformes.enums.StatusPagamento;
import br.com.sjduniformes.enums.StatusPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroPedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime dataCriacao;
    private LocalDate dataEntrega;

    private BigDecimal valorTotal;
    private BigDecimal valorPago;
    private BigDecimal valorDesconto;
    private BigDecimal valorAcrescimo;

    private String observacoesGerais;
    private String logoEmpresaUrl;
    private String imagemReferenciaUrl;

    private StatusPedido statusPedido;
    private StatusPagamento statusPagamento;
    private EtapaProducao etapaProducao;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> itens;

    @PrePersist
    public void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (statusPedido == null) {
            statusPedido = StatusPedido.ORCAMENTO;
        }
        if (statusPagamento == null) {
            statusPagamento = StatusPagamento.NAO_PAGO;
        }
        if (etapaProducao == null) {
            etapaProducao = EtapaProducao.ARTE;
        }
        if (valorPago == null) {
            valorPago = BigDecimal.ZERO;
        }
    }
}
