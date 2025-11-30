package br.com.sjduniformes.enums;

/**
 * Situação do pagamento do pedido.
 */
public enum StatusPagamento {
    NAO_PAGO,   // Nenhum valor foi pago.
    PAGO_50,    // Metade do valor foi quitada.
    PAGO_TOTAL  // Pagamento concluído integralmente.
}
