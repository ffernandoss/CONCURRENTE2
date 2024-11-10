package org.example.concurrente2.Datos;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "valor_exponencial")
public class ValorExponencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double valor;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}