package org.example.concurrente2.Datos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface
ValorNormalRepository extends JpaRepository<ValorNormal, Long> {
}
