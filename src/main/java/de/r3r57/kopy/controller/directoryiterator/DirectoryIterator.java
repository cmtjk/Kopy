package de.r3r57.kopy.controller.directoryiterator;

import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.view.AlertDialog;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DirectoryIterator {

    private List<String> eventList = new ArrayList<>();

    public List<String> getDirectories(String year) {

        eventList.clear();

        try (DirectoryStream<Path> dirStream = Files
                .newDirectoryStream(Paths.get(System.getProperty("user.dir") + "/../Bilder/"))) {

            dirStream.forEach(dir -> {
                if (year.equals(dir.getFileName().toString())) {

                    getEvents(dir.toAbsolutePath());
                }
            });

        } catch (IOException e) {
            AlertDialog.showErrorDialog("Fehler beim Durchsuchen!", e.getMessage(), e);
            KopyLogger.logger.error("unable to browse directory");
        }
        return eventList;
    }

    private void getEvents(Path dir) {

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {

            dirStream.forEach(event -> {
                if (Files.isDirectory(event)) {
                    eventList.add(event.getFileName().toString());

                }
            });

        } catch (IOException e) {
            AlertDialog.showErrorDialog("Fehler beim durchsuchen der Veranstaltungen!", e.getMessage(), e);
            KopyLogger.logger.error("unable to browse events");
        }
    }
}
