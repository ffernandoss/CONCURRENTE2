package org.example.concurrente2.Datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValorExponencialRepository extends JpaRepository<ValorExponencial, Long> {
}