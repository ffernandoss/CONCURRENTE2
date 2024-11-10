package org.example.concurrente2.webflux;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.concurrente2.ValorNormal;
import org.example.concurrente2.ValorNormalRepository;
import org.example.concurrente2.WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CsvService {

    private static final Logger logger = LoggerFactory.getLogger(CsvService.class);

    @Autowired
    private ValorNormalRepository valorNormalRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private final AtomicBoolean stopFlag = new AtomicBoolean(false);
    private int lastProcessedLine = 0;

    @Transactional
    public void clearDatabase() {
        valorNormalRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE valor_normal AUTO_INCREMENT = 1").executeUpdate();
        logger.info("Base de datos vaciada y auto-increment reiniciado");
    }

    @Transactional
    public Flux<ValorNormal> loadCsvData(String filePath) {
        logger.info("Starting to load CSV data from file: " + filePath);
        stopFlag.set(false);
        return Flux.create(sink -> {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                String[] line;
                int currentLine = 0;
                while ((line = reader.readNext()) != null) {
                    if (currentLine++ < lastProcessedLine) continue;
                    if (stopFlag.get()) {
                        lastProcessedLine = currentLine;
                        sink.complete();
                        break;
                    }
                    ValorNormal valorNormal = new ValorNormal();
                    valorNormal.setValor(Double.parseDouble(line[0]));
                    valorNormalRepository.save(valorNormal);
                    rabbitTemplate.convertAndSend("databaseQueue", "Nuevo valor cargado en la base de datos: ID = " + valorNormal.getId() + ", Valor = " + valorNormal.getValor());
                    WebSocketHandler.sendMessageToAll("{\"id\": " + valorNormal.getId() + ", \"valor\": " + valorNormal.getValor() + "}");
                    sink.next(valorNormal);
                    Thread.sleep(3000); // Delay of 3 seconds
                }
                sink.complete();
            } catch (IOException | CsvValidationException | InterruptedException e) {
                logger.error("Error reading CSV file", e);
                sink.error(e);
            }
        });
    }

    public void stopLoading() {
        stopFlag.set(true);
    }

    public void restartLoading() {
        stopFlag.set(false);
        loadCsvData("src/main/resources/datos_normales.csv").subscribe();
    }
}