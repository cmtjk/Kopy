package de.r3r57.kopy.model;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.model.filewriter.DescriptionWriter;
import de.r3r57.kopy.model.filewriter.FileCopier;
import de.r3r57.kopy.model.filewriter.UserFileWriter;
import de.r3r57.kopy.view.AlertDialog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MainModel extends Observable {

    private Set<String> events;
    private List<String> userList;
    private Set<File> filesList;
    private long fileSize = 0;
    private String destPath = "";
    private String user, year, description;
    private String event = "";
    private String addedFiles = "";
    private String copyingFile = "";
    private double percentPerFile = 0.0;
    private DoubleProperty progressProperty;
    private StringProperty fileProperty;
    private UserFileWriter userFileWriter;

    public MainModel() {

        userList = new ArrayList<>();
        filesList = new HashSet<>();
        events = new HashSet<>();
        destPath = System.getProperty("user.dir") + "/../Bilder/";
        double progress = 0.0;
        progressProperty = new SimpleDoubleProperty(progress);
        fileProperty = new SimpleStringProperty(copyingFile);
        userFileWriter = new UserFileWriter();

        KopyLogger.logger.info("model initialization: success");

    }

    public List<String> getUsers() {
        return userList;

    }

    public void setUsers(List<String> readUserFile) {
        if (readUserFile != null && readUserFile.size() > 0) {
            userList = readUserFile;
            setChanged();
            notifyObservers();
            KopyLogger.logger.info("users set: successfull");
        } else {
            AlertDialog.showErrorDialog("Nutzer konnten nicht geladen werden!", "", new IllegalArgumentException());
            KopyLogger.logger.info("unable to set users");
        }
    }

    public String getCopyingFile() {
        return copyingFile;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        if (user != null && !"".equals(user)) {
            this.user = user;
            KopyLogger.logger.info("current user: " + user);
        }
    }

    public void addUser(String name) {
        if (name != null && !"".equals(name) && name.length() <= 30 && !userList.contains(name)) {
            userList.add(name);
            user = name;
            userFileWriter.writeUserFile(name);
            userList.remove(0);
            Collections.sort(userList);
            userList.add(0, "Neuer Nutzer...");
            setChanged();
            notifyObservers();
            KopyLogger.logger.info("new user added: " + name);
        } else {
            AlertDialog.showErrorDialog("Nutzer konnte nicht hinzugefügt werden!", "",
                    new IllegalArgumentException("Username already exists!"));
            KopyLogger.logger.error("unable to add new user: " + name);
        }
    }

    public void addFiles(List<File> files) {
        if (files != null && files.size() > 0) {
            files.forEach(file -> {
                if (Files.exists(file.toPath())) {
                    filesList.add(file);
                }
            });
            setChanged();
            notifyObservers();
            KopyLogger.logger.info("new files added (" + files.size() + ")");
        } else {
            AlertDialog.showErrorDialog("Dateien konnten nicht hinzugefügt werden!", "",
                    new IllegalArgumentException());
            KopyLogger.logger.info("unable to add files");
        }
    }

    public List<String> getFilesAsString() {
        List<String> filesAsString = new ArrayList<>();
        getFiles().forEach(file -> filesAsString.add(file.toString())
        );
        return filesAsString;
    }

    private Set<File> getFiles() {
        return filesList;
    }

    public void clearFilesList() {
        filesList.clear();
        setChanged();
        notifyObservers();
        KopyLogger.logger.info("files list cleared: success");
    }

    public int getFilesSize() {
        fileSize = 0;
        filesList.forEach(file -> fileSize += file.length());
        return (int) fileSize / 1000000;
    }

    private boolean createDestination(Path destinationPath) throws IOException {

        if (!Files.exists(destinationPath)) {
            Files.createDirectories(destinationPath);
            Files.createFile(Paths.get(destinationPath.toString() + "/#_Beschreibung.txt"));
            KopyLogger.logger.info("event added: " + event);
            KopyLogger.logger.info("destination created: " + destinationPath.toAbsolutePath());
        }
        return true;
    }

    public void copyFiles() {

        Path destinationPath = Paths.get(destPath + "/" + year + "/" + event + "/" + user + "/");

        try {
            if (createDestination(destinationPath)) {
                String addedFiles = new FileCopier(this).copyFiles(destinationPath, filesList);
                new DescriptionWriter(destinationPath, year, event, user, description, addedFiles);

                Platform.runLater(() -> {
                    setProgressProperty(1);
                    setFileProperty("Fertig!");
                });
            }
        } catch (IOException e) {
            AlertDialog.showErrorDialog("Verzeichnis konnte nicht angelegt werden!", e.getMessage(), e);
            KopyLogger.logger.error("unable to create directory: " + e.getMessage());
        }

    }

    public void setValues(String user, String event, String year, String description) {
        if (user != null && event != null && year != null && description != null) {
            this.user = user;
            this.event = event;
            this.year = year;
            this.description = description;
        } else {
            AlertDialog.showErrorDialog("Ungültige Daten!", "", new NullPointerException());
            KopyLogger.logger.error("unable to set event information");
        }
    }

    public Set<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> eventList) {
        if (eventList != null) {
            events.clear();
            eventList.forEach(events::add);
            setChanged();
            notifyObservers("events");
        } else {
            AlertDialog.showErrorDialog("Ungültige Daten!", "", new NullPointerException());
            KopyLogger.logger.error("unable to set events");
        }
    }

    public void addEvent(String event) {
        if (event != null) {
            events.add(event);
            this.event = event;
            setChanged();
            notifyObservers("events");
        } else {
            AlertDialog.showErrorDialog("Ungültige Daten!", "", new NullPointerException());
            KopyLogger.logger.error("unable to add event");
        }
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        if (event != null) {
            this.event = event;
        } else {
            AlertDialog.showErrorDialog("Ungültige Daten!", "", new NullPointerException());
        }
    }

    public void deleteFiles(ObservableList<String> selectedItems) {
        if (selectedItems != null && selectedItems.size() > 0) {
            selectedItems.forEach(fileToDelete -> filesList.remove(Paths.get(fileToDelete).toFile()));
            setChanged();
            notifyObservers();
            KopyLogger.logger.info("file(s) removed from list: success");
        }

    }

    public DoubleProperty getProgressProperty() {
        return progressProperty;
    }

    public StringProperty getFileProperty() {
        return fileProperty;
    }

    public void setFileProperty(String fileProperty) {
        this.fileProperty.set(fileProperty);
    }

    public void setProgressProperty(double progressProperty) {
        this.progressProperty.set(progressProperty);
    }
}
