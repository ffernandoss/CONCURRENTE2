package org.example.concurrente2.webflux;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class CsvController {
    private final CsvService csvService;

    public CsvController(CsvService csvService) {
        this.csvService = csvService;
    }

    @PostMapping("/stop-loading-normal")
    public Mono<Void> stopLoadingNormal() {
        csvService.stopLoadingNormal();
        return Mono.empty();
    }

    @PostMapping("/stop-loading-exponential")
    public Mono<Void> stopLoadingExponential() {
        csvService.stopLoadingExponential();
        return Mono.empty();
    }

    @PostMapping("/resume-loading-normal")
    public Mono<Void> resumeLoadingNormal() {
        csvService.resumeLoadingNormal();
        return Mono.empty();
    }

    @PostMapping("/resume-loading-exponential")
    public Mono<Void> resumeLoadingExponential() {
        csvService.resumeLoadingExponential();
        return Mono.empty();
    }
}