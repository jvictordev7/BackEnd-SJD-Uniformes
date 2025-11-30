package br.com.sjduniformes.repository;

import br.com.sjduniformes.entity.Pedido;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    Optional<Pedido> findTopByOrderByIdDesc();
}
