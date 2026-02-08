package com.ra12.projecte1.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

/**
 * Component personalitzat per a la gestió de Logs en fitxers.
 * Crea un fitxer diari i registra esdeveniments INFO i ERROR.
 */
@Component
public class CustomLogging {

    private static final String LOGS_DIR = "logs";
    private static final String LOG_FILE_PREFIX = "aplicacio-";
    private static final String LOG_FILE_EXTENSION = ".log";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void info(String classe, String method, String description) {
        escriureLog("INFO", classe, method, description);
    }

    public void error(String classe, String method, String description) {
        escriureLog("ERROR", classe, method, description);
    }

    private String obtenirFitxerAvui() {
        String dataAvui = LocalDate.now().toString(); 
        return LOGS_DIR + File.separator + LOG_FILE_PREFIX + dataAvui + LOG_FILE_EXTENSION;
    }

    private void escriureLog(String nivell, String classe, String method, String description) {
        File directory = new File(LOGS_DIR);
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fitxerAvui = obtenirFitxerAvui();
        Path currentFile = Paths.get(fitxerAvui);

        try (BufferedWriter writer = Files.newBufferedWriter(
                currentFile,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            
            String timeStamp = LocalDateTime.now().format(dateTimeFormatter);
            String entradaLog = String.format("[%s] %s - %s - %s - %s", timeStamp, nivell, classe, method, description);
            
            writer.write(entradaLog);
            writer.newLine();
            System.out.println(entradaLog); // Mostrem també per consola
            
        } catch (Exception e) {
            System.err.println("Error escrivint al log: " + e.getMessage());
        }
    }
}