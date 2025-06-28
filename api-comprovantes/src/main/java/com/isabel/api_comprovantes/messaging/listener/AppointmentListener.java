package com.isabel.api_comprovantes.messaging.listener;

import com.isabel.api_comprovantes.messaging.dto.AppointmentDTO;
import com.isabel.api_comprovantes.messaging.dto.AppointmentEvent;
import com.isabel.api_comprovantes.service.DocumentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AppointmentListener {

    private static final Logger log = LoggerFactory.getLogger(AppointmentListener.class);

    private final DocumentService documentService;

    public AppointmentListener(DocumentService documentService) {
        this.documentService = documentService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.appointment.name}")
    public void consumeAppointmentEvent(AppointmentEvent event) {
        try {
            AppointmentDTO dto = new AppointmentDTO(
                    event.id(),
                    event.patientName(),
                    event.doctorName(),
                    event.dateTime().split("T")[0],
                    event.dateTime().split("T")[1]
            );

            String pdfUrl = documentService.generateAndUploadPdf(dto);
            log.info("PDF successfully generated for appointment: {}", pdfUrl);
        } catch (Exception e) {
            log.error("Error while generating PDF for appointment: {}", event.id(), e);
        }
    }
}