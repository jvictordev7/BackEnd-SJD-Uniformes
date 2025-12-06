package br.com.sjduniformes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para login do usuário")
public class LoginRequest {
    @Schema(description = "Email do usuário", example = "usuario@email.com")
    private String email;
    @Schema(description = "Senha do usuário", example = "senha123")
    private String senha;
}
