package br.com.sjduniformes.service;

import br.com.sjduniformes.dto.CaixaResumoDTO;
import br.com.sjduniformes.dto.CaixaSaidaRequest;
import br.com.sjduniformes.entity.CaixaLancamento;
import br.com.sjduniformes.enums.TipoLancamentoCaixa;
import br.com.sjduniformes.repository.CaixaLancamentoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaixaLancamentoService {

    private final CaixaLancamentoRepository caixaLancamentoRepository;

    public List<CaixaLancamento> listarTodos() {
        return caixaLancamentoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataLancamento"));
    }

    public CaixaResumoDTO calcularResumo() {
        List<CaixaLancamento> lista = caixaLancamentoRepository.findAll();

        BigDecimal totalEntradas = BigDecimal.ZERO;
        BigDecimal totalSaidas = BigDecimal.ZERO;

        for (CaixaLancamento lancamento : lista) {
            if (lancamento.getTipo() == TipoLancamentoCaixa.SAIDA) {
                totalSaidas = totalSaidas.add(nullSafe(lancamento.getValor()));
            } else {
                totalEntradas = totalEntradas.add(nullSafe(lancamento.getValor()));
            }
        }

        BigDecimal saldoTotal = totalEntradas.subtract(totalSaidas);
        return new CaixaResumoDTO(saldoTotal, totalEntradas, totalSaidas);
    }

    public CaixaLancamento registrarSaida(CaixaSaidaRequest request) {
        CaixaLancamento lanc = CaixaLancamento.builder()
            .dataLancamento(LocalDateTime.now())
            .descricao(request.getDescricao())
            .formaPagamento(request.getFormaPagamento())
            .origemTipo("MANUAL")
            .origemId(null)
            .tipo(TipoLancamentoCaixa.SAIDA)
            .valor(request.getValor())
            .build();

        return caixaLancamentoRepository.save(lanc);
    }

    private BigDecimal nullSafe(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
