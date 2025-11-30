package br.com.sjduniformes.entity;

import br.com.sjduniformes.enums.TipoLancamentoCaixa;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caixa_lancamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaixaLancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataLancamento;
    // Persiste o enum como texto no banco (ENTRADA/SAIDA).
    @Enumerated(EnumType.STRING)
    private TipoLancamentoCaixa tipo;
    private BigDecimal valor;
    private String descricao;
    private String formaPagamento;
    private String origemTipo;
    private Long origemId;

    @PrePersist
    public void onCreate() {
        if (dataLancamento == null) {
            dataLancamento = LocalDateTime.now();
        }
    }
}
