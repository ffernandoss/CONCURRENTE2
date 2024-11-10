package org.example.concurrente2.webflux;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.concurrente2.Datos.ValorExponencial;
import org.example.concurrente2.Datos.ValorExponencialRepository;
import org.example.concurrente2.Datos.ValorNormal;
import org.example.concurrente2.Datos.ValorNormalRepository;
import org.example.concurrente2.WebSocket.WebSocketHandler;
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
    private ValorExponencialRepository valorExponencialRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    private final AtomicBoolean stopNormalFlag = new AtomicBoolean(false);
    private final AtomicBoolean stopExponentialFlag = new AtomicBoolean(false);
    private int lastProcessedLine = 0;
    private int processedLines = 0;

    @Transactional
    public void clearDatabase() {
        valorNormalRepository.deleteAll();
        valorExponencialRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE valor_normal AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE valor_exponencial AUTO_INCREMENT = 1").executeUpdate();
        logger.info("Base de datos vaciada y auto-increment reiniciado");
    }

    @Transactional
    public Flux<ValorNormal> loadCsvData(String filePath) {
        logger.info("Iniciando la carga de datos CSV desde el archivo: " + filePath);
        stopNormalFlag.set(false);
        processedLines = 0;
        return Flux.create(sink -> {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                String[] line;
                int currentLine = 0;
                while ((line = reader.readNext()) != null) {
                    if (currentLine++ < lastProcessedLine) continue;
                    if (stopNormalFlag.get()) {
                        lastProcessedLine = currentLine;
                        sink.complete();
                        break;
                    }
                    ValorNormal valorNormal = new ValorNormal();
                    valorNormal.setValor(Double.parseDouble(line[0]));
                    valorNormalRepository.save(valorNormal);
                    rabbitTemplate.convertAndSend("databaseQueue", " NORMAL Nuevo valor cargado en la base de datos: ID = " + valorNormal.getId() + ", Valor = " + valorNormal.getValor());
                    WebSocketHandler.sendMessageToAll("{\"id\": " + valorNormal.getId() + ", \"valorN\": " + valorNormal.getValor() + "}");
                    sink.next(valorNormal);
                    processedLines++;
                    if (processedLines % 100 == 0) {
                        rabbitTemplate.convertAndSend("databaseQueue", processedLines + " datos guardados de la normal");
                    }
                    Thread.sleep(200);
                }
                sink.complete();
            } catch (IOException | CsvValidationException | InterruptedException e) {
                sink.error(e);
            }
        });
    }

    @Transactional
    public Flux<ValorExponencial> loadExponentialCsvData(String filePath) {
        logger.info("Iniciando la carga de datos exponenciales CSV desde el archivo: " + filePath);
        stopExponentialFlag.set(false);
        processedLines = 0;
        return Flux.create(sink -> {
            try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
                String[] line;
                int currentLine = 0;
                while ((line = reader.readNext()) != null) {
                    if (currentLine++ == 0) continue; // Skip header line
                    if (stopExponentialFlag.get()) {
                        lastProcessedLine = currentLine;
                        sink.complete();
                        break;
                    }
                    ValorExponencial valorExponencial = new ValorExponencial();
                    valorExponencial.setValor(Double.parseDouble(line[0]));
                    valorExponencialRepository.save(valorExponencial);
                    rabbitTemplate.convertAndSend("databaseQueue", " EXPONENCIAL Nuevo valor cargado en la base de datos: ID = " + valorExponencial.getId() + ", Valor = " + valorExponencial.getValor());
                    WebSocketHandler.sendMessageToAll("{\"id\": " + valorExponencial.getId() + ", \"valorE\": " + valorExponencial.getValor() + "}");
                    sink.next(valorExponencial);
                    processedLines++;
                    if (processedLines % 100 == 0) {
                        rabbitTemplate.convertAndSend("databaseQueue", processedLines + " datos guardados de la exponencial");
                    }
                    Thread.sleep(10);
                }
                sink.complete();
            } catch (IOException | CsvValidationException | InterruptedException e) {
                sink.error(e);
            }
        });
    }

    public void stopLoadingNormal() {
        stopNormalFlag.set(true);
    }

    public void stopLoadingExponential() {
        stopExponentialFlag.set(true);
    }

    public void resumeLoadingNormal() {
        stopNormalFlag.set(false);
        loadCsvData("src/main/resources/datos_normales.csv").subscribe();
    }

    public void resumeLoadingExponential() {
        stopExponentialFlag.set(false);
        loadExponentialCsvData("src/main/resources/distribucion_exponencial.csv").subscribe();
    }
}