package br.com.sjduniformes.repository;

import br.com.sjduniformes.entity.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
}
