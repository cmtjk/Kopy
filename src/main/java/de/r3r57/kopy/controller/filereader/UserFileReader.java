package de.r3r57.kopy.controller.filereader;

import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.view.AlertDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserFileReader {

    public UserFileReader() {

    }

    public List<String> readUserFile() {

        Path userFilePath = Paths.get(System.getProperty("user.dir") + "/config/userfile.dat");
        String line;
        List<String> nameList = new ArrayList<>();

        try (BufferedReader fileReader = Files.newBufferedReader(userFilePath, StandardCharsets.UTF_8)) {

            while ((line = fileReader.readLine()) != null) {
                nameList.add(line);
            }
            Collections.sort(nameList);
            nameList.add(0, "Neuer Nutzer...");
            KopyLogger.logger.info("reading user file: success");
            return nameList;
        } catch (IOException e) {
            AlertDialog.showErrorDialog("Datei konnte nicht gelesen werden!", e.getMessage(), e);
            KopyLogger.logger.error("unable to read user file");
        }
        return nameList;
    }
}
