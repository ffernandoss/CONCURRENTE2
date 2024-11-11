package org.example.concurrente2.Inicializadores;

import org.example.concurrente2.webflux.CsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private CsvService csvService;

    @Override
    public void run(String... args) throws Exception {
        csvService.clearDatabase();
        csvService.loadCsvData("src/main/resources/datos_normales.csv").subscribe();
    }
}