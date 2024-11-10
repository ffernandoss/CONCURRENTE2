package org.example.concurrente2.Datos;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "valor_normal")
public class ValorNormal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor")
    private Double valor;

    public ValorNormal() {
    }

    public ValorNormal(Double valor) {
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
