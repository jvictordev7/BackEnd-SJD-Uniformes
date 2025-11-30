package br.com.sjduniformes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pedidos_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotalItem;
    private String tamanho;
    private String corTecido;
    private String tipoServico;
    private String detalhesPersonalizacao;

    @PrePersist
    @PreUpdate
    public void calcularValorTotal() {
        if (quantidade != null && valorUnitario != null) {
            valorTotalItem = valorUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
}
