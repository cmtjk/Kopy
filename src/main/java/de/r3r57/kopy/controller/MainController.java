package de.r3r57.kopy.controller;

import javafx.collections.ObservableList;
import de.r3r57.kopy.controller.directoryiterator.DirectoryIterator;
import de.r3r57.kopy.controller.filereader.UserFileReader;
import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.model.MainModel;
import de.r3r57.kopy.view.MainView;
import de.r3r57.kopy.view.NewEventView;
import de.r3r57.kopy.view.NewUserView;
import de.r3r57.kopy.view.ReviewView;

import java.io.File;
import java.util.List;

public class MainController {

    private MainModel model;
    private MainView view;
    private NewUserView changeUserView;
    private ReviewView reviewView;
    private UserFileReader userFileReader;
    private DirectoryIterator dirIterator;
    private NewEventView newEventView;

    public MainController(MainView view, MainModel model) {
        this.model = model;
        this.view = view;

        userFileReader = new UserFileReader();
        dirIterator = new DirectoryIterator();

        setUsers();

        view.show();

        KopyLogger.logger.info("controller initialization: success");

    }

    public void showNewUserDialog() {

        changeUserView = new NewUserView(new NewUserController(model));
        changeUserView.show();

    }

    public void showNewEventDialog() {

        newEventView = new NewEventView(new NewEventController(model));
        newEventView.show();

    }

    public void setUsers() {
        model.setUsers(userFileReader.readUserFile());
    }

    public List<String> getUser() {
        return model.getUsers();
    }

    public void addFiles(List<File> files) {
        model.addFiles(files);
    }

    public void setUser(String name) {
        model.setUser(name);
    }

    public void clearFilesList() {
        model.clearFilesList();
    }

    public int getFilesSize() {
        return model.getFilesSize();
    }

    public void copyFiles() {
        new Thread(model::copyFiles).start();
    }

    public void setValues(String user, String event, String year, String description) {
        model.setValues(user, event, year, description);
    }

    public void getEvents(String year) {
        model.setEvents(dirIterator.getDirectories(year));

    }

    public void setEvent(String event) {
        model.setEvent(event);
    }

    public void clearEvent() {
        model.setEvent("");
    }

    public void deleteFiles(ObservableList<String> selectedItems) {
        model.deleteFiles(selectedItems);

    }

}
