package br.com.sjduniformes.enums;

/**
 * Etapas do processo produtivo de um uniforme.
 */
public enum EtapaProducao {
    ARTE,              // Criação ou aprovação da arte.
    SUBLIMACAO_SILK,   // Impressão por sublimação ou silk.
    COSTURA,           // Montagem e costura das peças.
    EMBALAGEM,         // Conferência final e embalagem.
    ENTREGUE           // Pedido entregue ao cliente.
}
