package br.com.sjduniformes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SJD Uniformes API")
                        .description("API REST do sistema de gestão de pedidos, clientes, produtos e caixa da SJD Uniformes.")
                        .version("1.0.0"));
    }
}
