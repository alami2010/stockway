package com.jbd.stock.utils;

import com.jbd.stock.domain.Category;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;

public class Utils {

    public static ResponseEntity<Resource> getResourceResponseEntity(File file) {
        FileSystemResource resource = new FileSystemResource(file);
        // 2.
        MediaType mediaType = MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(mediaType);
        // 3
        ContentDisposition disposition = ContentDisposition
            // 3.2
            .inline() // or .attachment()
            // 3.1
            .filename(Objects.requireNonNull(resource.getFilename()))
            .build();
        headers.setContentDisposition(disposition);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    public static File generateCategoryList(List<Category> categories) throws IOException {
        File tempFile = File.createTempFile("categories-x", ".csv");
        String[] HEADERS = { "Id", "Code", "Nom" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            categories.forEach(category -> {
                try {
                    printer.printRecord(category.getId(), category.getCode(), category.getLibelle());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return tempFile;
    }

    public static File generateCsvFileForUploadArticle() throws IOException {
        File tempFile = File.createTempFile("article-x", ".csv");
        String[] HEADERS = { "Nom Article", "Qte", "Description", "prix ", "Catégorie" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS));
        printer.printRecord("example article", "20", "ceci est une description", "2000", "TEXTILE");
        printer.close();
        return tempFile;
    }
}
