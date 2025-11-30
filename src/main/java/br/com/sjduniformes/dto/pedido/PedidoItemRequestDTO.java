package br.com.sjduniformes.dto.pedido;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemRequestDTO {
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private String tamanho;
    private String corTecido;
    private String tipoServico;
    private String detalhesPersonalizacao;
}
