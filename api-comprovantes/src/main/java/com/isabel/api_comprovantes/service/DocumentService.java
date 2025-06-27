package com.isabel.api_comprovantes.service;

import com.isabel.api_comprovantes.messaging.dto.AppointmentDTO;
import com.isabel.api_comprovantes.repository.AppointmentRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DocumentService {

    private final TemplateEngine templateEngine;
    private final AppointmentRepository appointmentRepository;
    private final S3Client s3Client;

    @Value("${spring.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${spring.aws.s3.region}")
    private String region;

    public DocumentService(TemplateEngine templateEngine, AppointmentRepository appointmentRepository, S3Client s3Client) {
        this.templateEngine = templateEngine;
        this.appointmentRepository = appointmentRepository;
        this.s3Client = s3Client;
    }

    private byte[] generatePdfBytes(AppointmentDTO dto) {
        Context context = new Context();
        context.setVariable("patientName", dto.patientName());
        context.setVariable("doctorName", dto.doctorName());
        context.setVariable("date", dto.date());
        context.setVariable("time", dto.time());
        String issueDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        context.setVariable("issueDate", issueDate);

        String htmlContent = templateEngine.process("appointment-pdf.html", context);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setCharset(StandardCharsets.UTF_8.name());
            HtmlConverter.convertToPdf(htmlContent, baos, converterProperties);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error to generating pdf: " + e.getMessage());
        }
    }

    public String generateAndUploadPdf(AppointmentDTO dto) {
        byte[] pdfBytes = generatePdfBytes(dto);

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String key = "appointments/appointment-" + timestamp + ".pdf";

        String url = uploadPdfToS3(pdfBytes, key);

        appointmentRepository.findById(dto.id()).ifPresent(appointment -> {
            appointment.setPdfPath(url);
            appointmentRepository.save(appointment);
        });

        return url;
    }

    private String uploadPdfToS3(byte[] pdfBytes, String key) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}