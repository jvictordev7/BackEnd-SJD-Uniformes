package br.com.sjduniformes.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaixaSaidaRequest {
    private String descricao;
    private String formaPagamento; // DINHEIRO, PIX, CARTAO...
    private BigDecimal valor;
}
