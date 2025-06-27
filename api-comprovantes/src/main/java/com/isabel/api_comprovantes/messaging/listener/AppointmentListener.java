package com.isabel.api_comprovantes.messaging.listener;

import com.isabel.api_comprovantes.messaging.dto.AppointmentDTO;
import com.isabel.api_comprovantes.messaging.dto.AppointmentEvent;
import com.isabel.api_comprovantes.service.DocumentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentListener {

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

            System.out.println("PDF successfully generated for appointment: " + pdfUrl); // EXCLUIR DEPOIS
        } catch (Exception e) {
            System.err.println("Error while generating PDF for appointment: " + event.id());
            e.printStackTrace();
        }
    }
}