package br.com.sjduniformes.dto.pedido;

import java.math.BigDecimal;
import br.com.sjduniformes.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroPagamentoDTO {
    private BigDecimal valor;          // valor que o cliente está pagando agora
    private String formaPagamento;     // ex: DINHEIRO, PIX, CARTAO
    private StatusPagamento statusPagamento; // opcional: NAO_PAGO, PAGO_50, PAGO_TOTAL
}
