package br.com.sjduniformes.repository;

import br.com.sjduniformes.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
