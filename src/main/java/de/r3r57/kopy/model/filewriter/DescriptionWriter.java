package de.r3r57.kopy.model.filewriter;

import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.view.AlertDialog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DescriptionWriter {

    public DescriptionWriter(Path destinationPath, String year, String event, String user, String description, String addedFiles) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd. MMMM yyyy - HH:mm");
        Date date = new Date();

        try (BufferedWriter bf = Files.newBufferedWriter(
                Paths.get(destinationPath.toString() + "/" + "#_Beschreibung.txt"), StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)
        ) {

            bf.write("\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592 Update: "
                    + dateFormat.format(date.getTime())
                    + " \u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592\u2592");
            bf.newLine();
            bf.write("Nutzer: " + user);
            bf.newLine();
            bf.write("Dateipfad: " + destinationPath.toString());
            bf.newLine();
            bf.newLine();
            bf.write(event + " (" + year + ")");
            bf.newLine();
            bf.write(description);
            bf.newLine();
            bf.newLine();
            bf.write("Hinzugefügte Dateien:");
            bf.write(addedFiles);
            bf.newLine();
            bf.newLine();

            bf.flush();

            KopyLogger.logger.info("description saved: success");

        } catch (IOException e) {
            AlertDialog.showErrorDialog("Beschreibung konnte nicht gespeichert werden!", e.getMessage(), e);
            KopyLogger.logger.error("unable to write description: " + e.getMessage());
        }
    }
}
