package br.com.sjduniformes.service;

import br.com.sjduniformes.dto.pedido.PedidoItemRequestDTO;
import br.com.sjduniformes.dto.pedido.PedidoResponseDTO;
import br.com.sjduniformes.dto.pedido.PedidoResponseDTO.PedidoItemResponseDTO;
import br.com.sjduniformes.dto.pedido.PedidoRequestDTO;
import br.com.sjduniformes.dto.pedido.RegistroPagamentoDTO;
import br.com.sjduniformes.entity.CaixaLancamento;
import br.com.sjduniformes.entity.Cliente;
import br.com.sjduniformes.entity.Pedido;
import br.com.sjduniformes.entity.PedidoItem;
import br.com.sjduniformes.entity.Produto;
import br.com.sjduniformes.enums.EtapaProducao;
import br.com.sjduniformes.enums.StatusPagamento;
import br.com.sjduniformes.enums.StatusPedido;
import br.com.sjduniformes.enums.TipoLancamentoCaixa;
import br.com.sjduniformes.repository.CaixaLancamentoRepository;
import br.com.sjduniformes.repository.ClienteRepository;
import br.com.sjduniformes.repository.PedidoItemRepository;
import br.com.sjduniformes.repository.PedidoRepository;
import br.com.sjduniformes.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final CaixaLancamentoRepository caixaLancamentoRepository;

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + request.getClienteId()));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(gerarNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setDataEntrega(request.getDataEntrega());
        pedido.setObservacoesGerais(request.getObservacoesGerais());
        pedido.setLogoEmpresaUrl(request.getLogoEmpresaUrl());
        pedido.setImagemReferenciaUrl(request.getImagemReferenciaUrl());
        pedido.setStatusPedido(request.getStatusPedido() != null ? request.getStatusPedido() : StatusPedido.ORCAMENTO);
        pedido.setStatusPagamento(request.getStatusPagamento() != null ? request.getStatusPagamento() : StatusPagamento.NAO_PAGO);
        pedido.setEtapaProducao(request.getEtapaProducao() != null ? request.getEtapaProducao() : EtapaProducao.ARTE);
        pedido.setValorDesconto(valorOuZero(request.getValorDesconto()));
        pedido.setValorAcrescimo(valorOuZero(request.getValorAcrescimo()));
        pedido.setValorPago(BigDecimal.ZERO);

        List<PedidoItem> itens = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        if (request.getItens() != null) {
            for (PedidoItemRequestDTO itemRequest : request.getItens()) {
                Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemRequest.getProdutoId()));

                PedidoItem item = new PedidoItem();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemRequest.getQuantidade());
                item.setValorUnitario(itemRequest.getValorUnitario());
                item.setTamanho(itemRequest.getTamanho());
                item.setCorTecido(itemRequest.getCorTecido());
                item.setTipoServico(itemRequest.getTipoServico());
                item.setDetalhesPersonalizacao(itemRequest.getDetalhesPersonalizacao());
                item.setValorTotalItem(calcularValorItem(itemRequest.getQuantidade(), itemRequest.getValorUnitario()));

                subtotal = subtotal.add(valorOuZero(item.getValorTotalItem()));
                itens.add(item);
            }
        }

        BigDecimal total = subtotal
            .subtract(valorOuZero(request.getValorDesconto()))
            .add(valorOuZero(request.getValorAcrescimo()));

        pedido.setValorTotal(total.max(BigDecimal.ZERO));
        pedido.setItens(itens);

        Pedido salvo = pedidoRepository.save(pedido);
        pedidoItemRepository.saveAll(itens);

        return converterParaResponse(salvo);
    }

    @Transactional
    public PedidoResponseDTO registrarPagamento(Long pedidoId, BigDecimal valor, String formaPagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + pedidoId));

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor do pagamento deve ser maior que zero");
        }

        BigDecimal total = valorOuZero(pedido.getValorTotal());
        BigDecimal valorPagoAtual = valorOuZero(pedido.getValorPago());
        BigDecimal novoValorPago = valorPagoAtual.add(valor);

        if (total.compareTo(BigDecimal.ZERO) > 0 && novoValorPago.compareTo(total) >= 0) {
            novoValorPago = total;
            pedido.setStatusPagamento(StatusPagamento.PAGO_TOTAL);
        } else if (novoValorPago.compareTo(BigDecimal.ZERO) <= 0) {
            pedido.setStatusPagamento(StatusPagamento.NAO_PAGO);
        } else {
            pedido.setStatusPagamento(StatusPagamento.PAGO_50);
        }

        pedido.setValorPago(novoValorPago);
        pedidoRepository.save(pedido);

        CaixaLancamento lancamento = new CaixaLancamento();
        lancamento.setTipo(TipoLancamentoCaixa.ENTRADA);
        lancamento.setValor(valor);
        lancamento.setDescricao("Pagamento do pedido " + pedido.getNumeroPedido());
        lancamento.setFormaPagamento(formaPagamento != null ? formaPagamento : "DESCONHECIDO");
        lancamento.setOrigemTipo("PEDIDO");
        lancamento.setOrigemId(pedido.getId());
        lancamento.setDataLancamento(LocalDateTime.now());
        caixaLancamentoRepository.save(lancamento);

        return converterParaResponse(pedido);
    }

    @Transactional
    public PedidoResponseDTO registrarPagamento(Long pedidoId, RegistroPagamentoDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados de pagamento são obrigatórios");
        }
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + pedidoId));

        BigDecimal valor = dto.getValor();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor do pagamento deve ser maior que zero");
        }

        BigDecimal total = valorOuZero(pedido.getValorTotal());
        BigDecimal valorPagoAtual = valorOuZero(pedido.getValorPago());
        BigDecimal novoValorPago = valorPagoAtual.add(valor);

        // Se status explícito veio no DTO, usa-o; senão calcula automaticamente.
        if (dto.getStatusPagamento() != null) {
            pedido.setStatusPagamento(dto.getStatusPagamento());
            if (dto.getStatusPagamento() == StatusPagamento.PAGO_TOTAL && total.compareTo(BigDecimal.ZERO) > 0 && novoValorPago.compareTo(total) >= 0) {
                novoValorPago = total;
            }
        } else {
            if (total.compareTo(BigDecimal.ZERO) > 0 && novoValorPago.compareTo(total) >= 0) {
                novoValorPago = total;
                pedido.setStatusPagamento(StatusPagamento.PAGO_TOTAL);
            } else if (novoValorPago.compareTo(BigDecimal.ZERO) <= 0) {
                pedido.setStatusPagamento(StatusPagamento.NAO_PAGO);
            } else {
                pedido.setStatusPagamento(StatusPagamento.PAGO_50);
            }
        }

        pedido.setValorPago(novoValorPago);
        pedidoRepository.save(pedido);

        CaixaLancamento lancamento = new CaixaLancamento();
        lancamento.setTipo(TipoLancamentoCaixa.ENTRADA);
        lancamento.setValor(valor);
        lancamento.setDescricao("Pagamento do pedido " + pedido.getNumeroPedido());
        lancamento.setFormaPagamento(dto.getFormaPagamento() != null ? dto.getFormaPagamento() : "DESCONHECIDO");
        lancamento.setOrigemTipo("PEDIDO");
        lancamento.setOrigemId(pedido.getId());
        lancamento.setDataLancamento(LocalDateTime.now());
        caixaLancamentoRepository.save(lancamento);

        return converterParaResponse(pedido);
    }

    @Transactional
    public PedidoResponseDTO alterarEtapa(Long pedidoId, EtapaProducao etapa) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + pedidoId));
        pedido.setEtapaProducao(etapa);
        pedidoRepository.save(pedido);
        return converterParaResponse(pedido);
    }

    @Transactional
    public PedidoResponseDTO atualizarPedido(Long pedidoId, PedidoRequestDTO request) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + pedidoId));

        Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado: " + request.getClienteId()));

        pedido.setCliente(cliente);
        pedido.setDataEntrega(request.getDataEntrega());
        pedido.setObservacoesGerais(request.getObservacoesGerais());
        pedido.setLogoEmpresaUrl(request.getLogoEmpresaUrl());
        pedido.setImagemReferenciaUrl(request.getImagemReferenciaUrl());
        pedido.setStatusPedido(request.getStatusPedido() != null ? request.getStatusPedido() : pedido.getStatusPedido());
        pedido.setStatusPagamento(request.getStatusPagamento() != null ? request.getStatusPagamento() : pedido.getStatusPagamento());
        pedido.setEtapaProducao(request.getEtapaProducao() != null ? request.getEtapaProducao() : pedido.getEtapaProducao());
        pedido.setValorDesconto(valorOuZero(request.getValorDesconto()));
        pedido.setValorAcrescimo(valorOuZero(request.getValorAcrescimo()));

        // Atualiza itens: remove antigos e insere novos (cascade com orphanRemoval cuida da exclusão).
        pedido.getItens().clear();

        List<PedidoItem> novosItens = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        if (request.getItens() != null) {
            for (PedidoItemRequestDTO itemRequest : request.getItens()) {
                Produto produto = produtoRepository.findById(itemRequest.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemRequest.getProdutoId()));

                PedidoItem item = new PedidoItem();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemRequest.getQuantidade());
                item.setValorUnitario(itemRequest.getValorUnitario());
                item.setTamanho(itemRequest.getTamanho());
                item.setCorTecido(itemRequest.getCorTecido());
                item.setTipoServico(itemRequest.getTipoServico());
                item.setDetalhesPersonalizacao(itemRequest.getDetalhesPersonalizacao());
                item.setValorTotalItem(calcularValorItem(itemRequest.getQuantidade(), itemRequest.getValorUnitario()));

                subtotal = subtotal.add(valorOuZero(item.getValorTotalItem()));
                novosItens.add(item);
            }
        }

        BigDecimal total = subtotal
            .subtract(valorOuZero(request.getValorDesconto()))
            .add(valorOuZero(request.getValorAcrescimo()));

        pedido.setValorTotal(total.max(BigDecimal.ZERO));
        pedido.setItens(novosItens);

        Pedido salvo = pedidoRepository.save(pedido);
        pedidoItemRepository.saveAll(novosItens);

        return converterParaResponse(salvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPorId(Long id) {
        return pedidoRepository.findById(id)
            .map(this::converterParaResponse)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
            .map(this::converterParaResponse)
            .toList();
    }

    @Transactional
    public void excluirPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado: " + id));

        // Cascade.ALL + orphanRemoval nos itens garante exclusão em cascata.
        pedidoRepository.delete(pedido);
    }

    private String gerarNumeroPedido() {
        return pedidoRepository.findTopByOrderByIdDesc()
            .map(Pedido::getNumeroPedido)
            .map(this::incrementarNumero)
            .orElse("PED-0001");
    }

    private String incrementarNumero(String numeroAtual) {
        try {
            String parteNumerica = numeroAtual.replace("PED-", "");
            int valor = Integer.parseInt(parteNumerica);
            return String.format("PED-%04d", valor + 1);
        } catch (NumberFormatException e) {
            return "PED-0001";
        }
    }

    private BigDecimal calcularValorItem(Integer quantidade, BigDecimal valorUnitario) {
        if (quantidade == null || valorUnitario == null) {
            return BigDecimal.ZERO;
        }
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    private BigDecimal valorOuZero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private PedidoResponseDTO converterParaResponse(Pedido pedido) {
        return new PedidoResponseDTO(
            pedido.getNumeroPedido(),
            pedido.getId(),
            pedido.getCliente() != null ? pedido.getCliente().getId() : null,
            pedido.getCliente() != null ? pedido.getCliente().getNome() : null,
            pedido.getDataCriacao(),
            pedido.getDataEntrega(),
            pedido.getValorTotal(),
            pedido.getValorPago(),
            pedido.getValorDesconto(),
            pedido.getValorAcrescimo(),
            pedido.getStatusPedido(),
            pedido.getStatusPagamento(),
            pedido.getEtapaProducao(),
            pedido.getObservacoesGerais(),
            pedido.getLogoEmpresaUrl(),
            pedido.getImagemReferenciaUrl(),
            pedido.getItens() != null ? pedido.getItens().stream().map(this::converterItemParaResponse).toList() : List.of()
        );
    }

    private PedidoItemResponseDTO converterItemParaResponse(PedidoItem item) {
        return new PedidoItemResponseDTO(
            item.getProduto() != null ? item.getProduto().getId() : null,
            item.getProduto() != null ? item.getProduto().getNome() : null,
            item.getQuantidade(),
            item.getValorUnitario(),
            item.getValorTotalItem(),
            item.getTamanho(),
            item.getCorTecido(),
            item.getTipoServico(),
            item.getDetalhesPersonalizacao()
        );
    }
}
