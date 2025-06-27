package com.isabel.api_comprovantes.messaging.listener;

import com.isabel.api_comprovantes.messaging.dto.AppointmentDTO;
import com.isabel.api_comprovantes.messaging.dto.AppointmentEvent;
import com.isabel.api_comprovantes.service.PdfService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AppointmentListener {

    private final PdfService pdfService;

    public AppointmentListener(PdfService pdfService) {
        this.pdfService = pdfService;
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

            byte[] pdf = pdfService.generatePdf(dto).getBytes();

            System.out.println("PDF successfully generated for appointment: " + event.id());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while generating PDF for appointment: " + event.id());
        }
    }
}