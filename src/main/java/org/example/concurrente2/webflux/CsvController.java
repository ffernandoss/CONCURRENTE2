package org.example.concurrente2.webflux;

import org.example.concurrente2.Datos.ValorNormal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class CsvController {

    private static final Logger logger = LoggerFactory.getLogger(CsvController.class);

    @Autowired
    private CsvService csvService;

    @GetMapping("/load-csv")
    public Flux<ValorNormal> loadCsv(@RequestParam String filePath) {
        logger.info("Received request to load CSV from file: " + filePath);
        return csvService.loadCsvData(filePath);
    }

    @PostMapping("/stop-loading")
    public void stopLoading() {
        logger.info("Received request to stop loading CSV data");
        csvService.stopLoading();
    }
    @PostMapping("/restart-loading")
    public void restartLoading() {
        logger.info("Received request to restart loading CSV data");
        csvService.restartLoading();
    }

}