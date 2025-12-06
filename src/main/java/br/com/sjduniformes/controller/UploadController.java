package br.com.sjduniformes.controller;

import br.com.sjduniformes.entity.Cliente;
import br.com.sjduniformes.entity.Produto;
import br.com.sjduniformes.repository.ClienteRepository;
import br.com.sjduniformes.repository.PedidoRepository;
import br.com.sjduniformes.repository.ProdutoRepository;
import br.com.sjduniformes.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Tag(name = "Upload", description = "Endpoints para upload de imagens e arquivos")
public class UploadController {

    private final CloudinaryService cloudinaryService;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @PostMapping("/produtos/{id}/imagem-principal")
    @Operation(summary = "Upload imagem principal do produto", description = "Faz upload da imagem principal de um produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagem uploadada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Arquivo inválido"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> uploadImagemPrincipalProduto(
        @Parameter(description = "ID do produto") @PathVariable Long id,
        @Parameter(description = "Arquivo de imagem") @RequestParam("file") MultipartFile file
    ) {
        return produtoRepository.findById(id)
            .map(produto -> {
                try {
                    String url = cloudinaryService.uploadImagem(file, "sjd_uniformes/produtos");
                    produto.setImagemPrincipal(url);
                    Produto salvo = produtoRepository.save(produto);
                    return ResponseEntity.ok(salvo);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (RuntimeException e) {
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/pedidos/{id}/logo-empresa")
    @Operation(summary = "Upload logo da empresa", description = "Faz upload do logo da empresa para um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logo uploadado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Arquivo inválido"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> uploadLogoEmpresa(
        @Parameter(description = "ID do pedido") @PathVariable Long id,
        @Parameter(description = "Arquivo de imagem") @RequestParam("file") MultipartFile file
    ) {
        return pedidoRepository.findById(id)
            .map(pedido -> {
                try {
                    String url = cloudinaryService.uploadImagem(file, "sjd_uniformes/pedidos/logos");
                    pedido.setLogoEmpresaUrl(url);
                    return ResponseEntity.ok(pedidoRepository.save(pedido));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (RuntimeException e) {
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/pedidos/{id}/imagem-referencia")
    @Operation(summary = "Upload imagem de referência", description = "Faz upload da imagem de referência para um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagem uploadada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Arquivo inválido"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> uploadImagemReferencia(
        @Parameter(description = "ID do pedido") @PathVariable Long id,
        @Parameter(description = "Arquivo de imagem") @RequestParam("file") MultipartFile file
    ) {
        return pedidoRepository.findById(id)
            .map(pedido -> {
                try {
                    String url = cloudinaryService.uploadImagem(file, "sjd_uniformes/pedidos/referencias");
                    pedido.setImagemReferenciaUrl(url);
                    return ResponseEntity.ok(pedidoRepository.save(pedido));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (RuntimeException e) {
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/clientes/{id}/foto")
    @Operation(summary = "Upload foto do cliente", description = "Faz upload da foto de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto uploadada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Arquivo inválido"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<?> uploadFotoCliente(
        @Parameter(description = "ID do cliente") @PathVariable Long id,
        @Parameter(description = "Arquivo de imagem") @RequestParam("file") MultipartFile file
    ) {
        return clienteRepository.findById(id)
            .map(cliente -> {
                try {
                    String url = cloudinaryService.uploadImagem(file, "sjd_uniformes/clientes");
                    cliente.setFoto(url);
                    Cliente salvo = clienteRepository.save(cliente);
                    return ResponseEntity.ok(salvo);
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (RuntimeException e) {
                    return ResponseEntity.internalServerError().body(e.getMessage());
                }
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
