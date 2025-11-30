package br.com.sjduniformes.dto.pedido;

import br.com.sjduniformes.enums.EtapaProducao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterarEtapaRequestDTO {
    private EtapaProducao etapaProducao;
}
