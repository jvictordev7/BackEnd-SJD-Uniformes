package br.com.sjduniformes.repository;

import br.com.sjduniformes.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
