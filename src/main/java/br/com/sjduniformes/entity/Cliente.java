package br.com.sjduniformes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String email;
    private Integer idade;
    private String foto;
    private String cpfCnpj;
    private String tipoPessoa;
    private String telefone;
    private String enderecoLogradouro;
    private String enderecoNumero;
    private String enderecoBairro;
    private String enderecoCidade;
    private String enderecoUf;
    private String enderecoCep;
    private String observacoes;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void onCreate() {
        dataCadastro = LocalDateTime.now();
        dataAtualizacao = dataCadastro;
    }

    @PreUpdate
    public void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
