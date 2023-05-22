package com.jbd.stock.utils;

import com.jbd.stock.domain.Article;
import com.jbd.stock.domain.Category;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        String[] HEADERS = { "Code", "Nom" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            categories.forEach(category -> {
                try {
                    printer.printRecord(category.getId(), category.getLibelle());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return tempFile;
    }

    public static File generateCsvFileForUploadArticle() throws IOException {
        File tempFile = File.createTempFile("article-x", ".csv");
        String[] HEADERS = { "Code Article", "Nom Article", "Qté", "prix ", "Code Catégorie", "Description" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS));
        printer.printRecord("PO0000001", "example article", "20", "2000", "2", "ceci est une description");
        printer.close();
        return tempFile;
    }

    public static File generateCsvFile(List<Article> colisList) throws IOException {
        File tempFile = File.createTempFile("article-x", ".csv");
        String[] HEADERS = { "Code", "Nom", "Prix", "Qte", "Code Categorie", "Category", "Description" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            colisList.forEach(article -> {
                Category category = article.getCategory();
                String idCat = "";
                String nomCat = "";
                if (category != null) {
                    idCat = category.getId().toString();
                    nomCat = category.getLibelle();
                }

                try {
                    printer.printRecord(
                        article.getId(),
                        article.getNom(),
                        article.getPrixAchat(),
                        article.getQte(),
                        idCat,
                        nomCat,
                        article.getDescription()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return tempFile;
    }

    public static File generateIventaireFile() throws IOException {
        File tempFile = File.createTempFile("inventaire-x", ".csv");
        String[] HEADERS = { "Code Article", "Qte" };

        FileWriter out = new FileWriter(tempFile, StandardCharsets.UTF_8);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS));
        printer.printRecord("1", "20");
        printer.close();
        return tempFile;
    }

    public static String getCode(Long id, Category category) {
        String prefix = category != null ? category.getLibelle().toUpperCase().substring(0, 2) : "AR";
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("ddMMyy");
        String currentDay = date.format(formatters);

        return prefix + currentDay + id.toString();
    }
}
