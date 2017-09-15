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

public class UserFileWriter {

    public void writeUserFile(String name) {

        Path userFilePath = Paths.get(System.getProperty("user.dir") + "/config/userfile.dat");

        try (BufferedWriter fileReader = Files.newBufferedWriter(userFilePath, StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            fileReader.write(name);
            fileReader.newLine();

        } catch (IOException e) {
            AlertDialog.showErrorDialog("Datei konnte nicht geschrieben werden!", e.getMessage(), e);
            KopyLogger.logger.error("unable to write user userfile: " + e.getMessage());
        }
    }
}
