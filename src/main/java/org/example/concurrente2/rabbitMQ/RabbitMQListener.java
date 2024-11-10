package org.example.concurrente2.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

    @RabbitListener(queues = "databaseQueue")
    public void listen(String message) {
        System.out.println("Mensaje recibido: " + message);
    }
}