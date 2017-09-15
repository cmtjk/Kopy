package de.r3r57.kopy.model.filewriter;

import javafx.application.Platform;
import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.model.MainModel;
import de.r3r57.kopy.view.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;

public class FileCopier {

    private MainModel model;
    private double progress = 0;

    public FileCopier(MainModel model) {
        this.model = model;
    }


    public String copyFiles(Path destinationPath, Set<File> filesList) {

        StringBuilder addedFiles = new StringBuilder();
        progress = 0;
        double percentPerFile = 100.0 / filesList.size();

        Platform.runLater(() -> {
            model.setFileProperty("");
            model.setProgressProperty(0.0);
        });


        filesList.forEach(file -> {
            try {
                Files.copy(file.toPath(), Paths.get(destinationPath.toString() + "/" + file.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
                addedFiles.append("\n").append(file.toString());
                progress++;
                Platform.runLater(() -> {
                    model.setFileProperty("kopying..." + file.getName());
                    model.setProgressProperty(percentPerFile * progress / 100);
                });

                KopyLogger.logger.info("file copied: " + file.getName());

            } catch (IOException e) {
                AlertDialog.showErrorDialog("Datei konnten nicht kopiert werden!", e.getMessage(), e);
                KopyLogger.logger.error("unable to copy files: " + e.getMessage());
                addedFiles.append("stopping...\nunable to copy file: ").append(file);
            }
        });

        KopyLogger.logger.info("files copied: success");

        return addedFiles.toString();

    }
}
