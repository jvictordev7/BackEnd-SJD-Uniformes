package br.com.sjduniformes.controller;

import br.com.sjduniformes.entity.Cliente;
import br.com.sjduniformes.entity.Produto;
import br.com.sjduniformes.repository.ClienteRepository;
import br.com.sjduniformes.repository.PedidoRepository;
import br.com.sjduniformes.repository.ProdutoRepository;
import br.com.sjduniformes.service.CloudinaryService;
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
public class UploadController {

    private final CloudinaryService cloudinaryService;
    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @PostMapping("/produtos/{id}/imagem-principal")
    public ResponseEntity<?> uploadImagemPrincipalProduto(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
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
    public ResponseEntity<?> uploadLogoEmpresa(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
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
    public ResponseEntity<?> uploadImagemReferencia(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
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
    public ResponseEntity<?> uploadFotoCliente(
        @PathVariable Long id,
        @RequestParam("file") MultipartFile file
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
