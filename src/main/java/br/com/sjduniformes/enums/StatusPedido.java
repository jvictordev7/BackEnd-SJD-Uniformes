package br.com.sjduniformes.enums;

/**
 * Estado atual de um pedido dentro do fluxo.
 */
public enum StatusPedido {
    /** Pedido é apenas um orçamento inicial, sem aprovação do cliente. */
    ORCAMENTO,
    /** Cliente aprovou o orçamento e autorizou a produção. */
    APROVADO,
    /** Pedido está em fase de produção. */
    EM_PRODUCAO,
    /** Produção concluída e pronto para entrega/retirada. */
    CONCLUIDO,
    /** Pedido foi cancelado pelo cliente ou pela empresa. */
    CANCELADO
}
