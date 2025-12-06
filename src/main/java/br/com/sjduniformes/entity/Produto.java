package br.com.sjduniformes.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidade que representa um produto no sistema")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Camiseta Polo")
    private String nome;
    @Schema(description = "Descrição detalhada do produto", example = "Camiseta polo branca tamanho M")
    private String descricao;
    private BigDecimal preco;
    private LocalDateTime dataAtualizado;
    private String tipoImpressao;
    private Boolean ativo;
    private String imagemPrincipal;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaProduto categoria;

    @PrePersist
    public void onCreate() {
        dataAtualizado = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        dataAtualizado = LocalDateTime.now();
    }
}
