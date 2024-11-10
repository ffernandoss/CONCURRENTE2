package org.example.concurrente2;

import org.example.concurrente2.ValorNormal;
import org.example.concurrente2.webflux.CsvController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class Concurrente2Application implements CommandLineRunner {

    @Autowired
    private CsvController csvController;

    public static void main(String[] args) {
        SpringApplication.run(Concurrente2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src/main/resources/datos_normales.csv"; // Reemplaza con la ruta de tu archivo CSV
        Flux<ValorNormal> flux = csvController.loadCsv(filePath);
        flux.subscribe(valorNormal -> {
            System.out.println("Valor: " + valorNormal.getValor());
        });
    }
}