package com.isabel.api_comprovantes.service;

import com.isabel.api_comprovantes.entity.Appointment;
import com.isabel.api_comprovantes.messaging.dto.AppointmentDTO;
import com.isabel.api_comprovantes.repository.AppointmentRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;
    private final AppointmentRepository appointmentRepository;

    public PdfService(TemplateEngine templateEngine, AppointmentRepository appointmentRepository) {
        this.templateEngine = templateEngine;
        this.appointmentRepository = appointmentRepository;
    }

    public String generatePdf(AppointmentDTO dto) throws Exception {
        Context context = new Context();
        context.setVariable("id", dto.id());
        context.setVariable("patientName", dto.patientName());
        context.setVariable("doctorName", dto.doctorName());
        context.setVariable("date", dto.date());
        context.setVariable("time", dto.time());

        String issueDate = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        context.setVariable("issueDate", issueDate);

        String htmlContent = templateEngine.process("appointment-pdf.html", context);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "pdfs/appointments-" + timestamp + ".pdf";

        File folder = new File("pdfs");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (OutputStream os = new FileOutputStream(filename)) {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset(String.valueOf(StandardCharsets.UTF_8));
            HtmlConverter.convertToPdf(htmlContent, os, converterProperties);
        }

        appointmentRepository.findById(dto.id()).ifPresent(appointment -> {
            appointment.setPdfPath(filename);
            appointmentRepository.save(appointment);
        });

        return filename;
    }

    public String getPdfPathByAppointmentId(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found for ID: " + appointmentId));

        String pdfPath = appointment.getPdfPath();

        if (pdfPath == null || pdfPath.isEmpty()) {
            throw new RuntimeException("PDF has not been generated for this appointment: " + appointmentId);
        }

        return pdfPath;
    }
}