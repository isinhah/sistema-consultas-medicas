package com.agendador.api_agendador.messaging.producer;

import com.agendador.api_agendador.messaging.dto.AppointmentEvent;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppointmentProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String routingKey;

    public AppointmentProducer(
            RabbitTemplate rabbitTemplate,
            @Value("${spring.rabbitmq.queues.appointment.name}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.routingKey = routingKey;
    }

    public void publishAppointmentEvent(AppointmentResponseDTO dto) {
        AppointmentEvent event = new AppointmentEvent(
                dto.id(),
                dto.patientName(),
                dto.doctorName(),
                dto.dateTime().toString()
        );

        rabbitTemplate.convertAndSend(routingKey, event);
    }
}