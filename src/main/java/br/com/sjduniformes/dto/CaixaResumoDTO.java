package br.com.sjduniformes.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaixaResumoDTO {
    private BigDecimal saldoTotal;
    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
}
