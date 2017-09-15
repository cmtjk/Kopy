package de.r3r57.kopy.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import de.r3r57.kopy.controller.MainController;
import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.model.MainModel;

import java.io.File;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.*;

@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
public class MainView extends Stage implements Observer {

    private final String version = "1.0";

    private MainModel model;
    private MainController controller;

    private ChoiceBox<String> userNames;

    private TextField yearTextField;

    private TextArea description;

    private Button addFiles;

    private Button deleteFiles;

    private ListView<String> filesList;

    private Button resetButton, copyButton;

    private Button continueButton, cancelButton;

    private VBox reviewVBox;

    private Label userName;

    private Label eventName;

    private Label yearName;

    private Label fileNumber;

    private Label fileSize;
    private TextArea descriptionName;

    private VBox mainVBox;
    private VBox progressVBox;

    private ProgressBar progressBar;

    private Label copiedFile;

    private Label percent;

    private Button finishButton;

    private ChoiceBox<String> eventChoiceBox;

    private DecimalFormat df = new DecimalFormat("#.#");

    public MainView(MainModel model) {
        this.model = model;

        setFrameOptions();

        Pane rootPane = createContentPane();
        initListenerAndEvents();
        createBindings();

        Scene scene = new Scene(rootPane, 550, 750);
        this.setScene(scene);

        rootPane.setStyle("-fx-font-size: 11pt");

        KopyLogger.logger.info("view initialization: success");

    }

    public void setController(MainController controller) {
        this.controller = controller;
        userNames.setItems(FXCollections.observableArrayList(controller.getUser()));
    }

    public void registerObserver() {
        model.addObserver(this);
    }

    private StackPane createContentPane() {

        StackPane mainPane = new StackPane();

        mainVBox = new VBox(10);
        mainVBox.setPadding(new Insets(10, 10, 10, 10));
        mainVBox.setStyle("-fx-background-color: rgba(0,0,0, 0.15)");

        mainVBox.getChildren().addAll(createHeader(), createUserLabel(), createEvent(), createDescription(),
                createFilesList(), createFooter());

        mainPane.getChildren().addAll(mainVBox, createProgress(), createReview());

        return mainPane;
    }

    private VBox createProgress() {

        progressVBox = new VBox(10);
        progressVBox.setStyle("-fx-background-color: rgba(0,0,0,.5)");
        progressVBox.setAlignment(Pos.CENTER);
        progressVBox.setPadding(new Insets(15, 15, 15, 15));

        copiedFile = new Label("");
        copiedFile.setTextFill(Color.WHITE);

        StackPane progressPane = new StackPane();

        percent = new Label("0%");

        progressBar = new ProgressBar();
        progressBar.setPrefWidth(Double.MAX_VALUE);

        progressPane.getChildren().addAll(progressBar, percent);

        finishButton = new Button("Zurück");
        finishButton.setDisable(true);

        progressVBox.getChildren().addAll(copiedFile, progressPane, finishButton);

        progressVBox.setVisible(false);

        return progressVBox;
    }

    private VBox createReview() {

        reviewVBox = new VBox(10);
        reviewVBox.setStyle("-fx-background-color: rgba(0,0,0,.5);");
        reviewVBox.setAlignment(Pos.CENTER);

        GridPane reviewGrid = new GridPane();

        reviewGrid.setHgap(4);
        reviewGrid.setVgap(6);
        reviewGrid.setPadding(new Insets(15, 15, 15, 15));
        reviewGrid.setPrefHeight(250);
        reviewGrid.setPrefWidth(500);

        Label userLabel = new Label("Nutzer:");
        userName = new Label("");
        Label eventLabel = new Label("Veranstaltung:");
        eventName = new Label("");
        Label yearLabel = new Label("Jahr:");
        yearName = new Label("");
        fileNumber = new Label("");
        fileSize = new Label("");

        userLabel.setTextFill(Color.WHITE);
        userName.setTextFill(Color.WHITE);
        eventLabel.setTextFill(Color.WHITE);
        eventName.setTextFill(Color.WHITE);
        yearLabel.setTextFill(Color.WHITE);
        yearName.setTextFill(Color.WHITE);
        fileNumber.setTextFill(Color.WHITE);
        fileSize.setTextFill(Color.WHITE);

        userName.setStyle("-fx-font-weight:bold;");
        eventName.setStyle("-fx-font-weight:bold;");
        yearName.setStyle("-fx-font-weight:bold;");

        descriptionName = new TextArea("");
        description.setPromptText("nähere Beschreibung (Ort/Zeitraum/...)");
        descriptionName.setEditable(false);
        descriptionName.setWrapText(true);
        descriptionName.setMaxWidth(Double.MAX_VALUE);
        descriptionName.setPrefHeight(450);

        GridPane.setConstraints(userLabel, 1, 1);
        GridPane.setConstraints(userName, 2, 1);
        GridPane.setConstraints(eventLabel, 1, 2);
        GridPane.setConstraints(eventName, 2, 2);
        GridPane.setConstraints(yearLabel, 1, 3);
        GridPane.setConstraints(yearName, 2, 3);
        GridPane.setConstraints(descriptionName, 1, 5, 5, 5);
        GridPane.setConstraints(fileNumber, 1, 10);
        GridPane.setConstraints(fileSize, 3, 10);

        reviewGrid.getChildren().addAll(userLabel, userName, eventLabel, eventName, yearLabel, yearName,
                descriptionName, fileNumber, fileSize);

        copyButton = new Button("Übertragen");
        cancelButton = new Button("Abbrechen");

        HBox buttonHBox = new HBox(25);
        buttonHBox.setAlignment(Pos.BASELINE_CENTER);
        buttonHBox.getChildren().addAll(cancelButton, copyButton);

        reviewVBox.getChildren().addAll(reviewGrid, buttonHBox);

        reviewVBox.setVisible(false);

        return reviewVBox;

    }

    private VBox createHeader() {

        DropShadow ds = new DropShadow();
        ds.setOffsetY(10.0);
        ds.setOffsetX(5.0);
        ds.setColor(Color.GRAY);

        VBox headerVBox = new VBox();
        headerVBox.setPadding(new Insets(20, 20, 20, 20));
        headerVBox.setAlignment(Pos.CENTER);

        Image kopyImage = new Image("file:" + System.getProperty("user.dir") + "/config/kopy.png");
        ImageView kopyLogo = new ImageView(kopyImage);

        kopyLogo.setFitWidth(200);
        kopyLogo.setPreserveRatio(true);
        kopyLogo.setSmooth(true);
        kopyLogo.setCache(true);

        kopyLogo.setEffect(ds);

        Label versionLabel = new Label(version);

        headerVBox.getChildren().addAll(kopyLogo, versionLabel);

        return headerVBox;
    }

    private HBox createUserLabel() {
        HBox userHBox = new HBox(10);
        userHBox.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Nutzer:");
        userLabel.setStyle("-fx-font-weight:bold;");
        userNames = new ChoiceBox<>();
        userNames.setPrefWidth(200);

        userHBox.getChildren().addAll(userLabel, userNames);
        return userHBox;
    }

    private TitledPane createEvent() {

        HBox eventHBox = new HBox(10);

        yearTextField = new TextField("");
        yearTextField.setPromptText("Jahr");

        eventChoiceBox = new ChoiceBox<>();
        eventChoiceBox.setDisable(true);
        eventChoiceBox.setPrefWidth(450);

        eventHBox.getChildren().addAll(yearTextField, eventChoiceBox);

        TitledPane eventPane = new TitledPane("Veranstaltung", eventHBox);
        eventPane.setCollapsible(false);

        return eventPane;
    }

    private TitledPane createDescription() {

        description = new TextArea();
        description.setWrapText(true);

        TitledPane descriptionPane = new TitledPane("Beschreibung", description);
        descriptionPane.setCollapsible(false);

        return descriptionPane;

    }

    private TitledPane createFilesList() {

        VBox filesVBox = new VBox(10);
        HBox buttonsHBox = new HBox(15);
        buttonsHBox.setAlignment(Pos.CENTER);

        addFiles = new Button("Dateien hinzufügen");
        deleteFiles = new Button("Ausgewählte entfernen");
        deleteFiles.setDisable(true);

        filesList = new ListView<>();
        filesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        buttonsHBox.getChildren().addAll(addFiles, deleteFiles);
        filesVBox.getChildren().addAll(buttonsHBox, filesList);

        TitledPane filesPane = new TitledPane("Dateien", filesVBox);
        filesPane.setCollapsible(false);
        filesPane.setPrefHeight(300);

        return filesPane;

    }

    private HBox createFooter() {

        HBox footerPane = new HBox(30);
        footerPane.setAlignment(Pos.BASELINE_RIGHT);
        footerPane.setPadding(new Insets(10, 10, 10, 10));

        resetButton = new Button("Zurücksetzen");
        continueButton = new Button("Weiter");
        continueButton.setPrefWidth(200);
        continueButton.setStyle("-fx-font-weight: bold;");

        footerPane.getChildren().addAll(resetButton, continueButton);

        return footerPane;
    }

    private void resetAll() {
        eventChoiceBox.getSelectionModel().clearSelection();
        yearTextField.setText("");
        description.setText("");
        controller.clearEvent();
        controller.clearFilesList();
        mainVBox.setEffect(null);
    }

    private void initListenerAndEvents() {

        userNames.getSelectionModel().selectedItemProperty().addListener(listener -> {
            if ("Neuer Nutzer...".equals(userNames.getSelectionModel().getSelectedItem())) {
                controller.showNewUserDialog();
                userNames.getSelectionModel().clearSelection();
            } else if (userNames.getSelectionModel().getSelectedItem() != null) {
                controller.setUser(userNames.getSelectionModel().getSelectedItem());
            }
        });

        yearTextField.textProperty().addListener(listener -> {
            if (yearTextField.getText().length() > 4) {
                yearTextField.setText(yearTextField.getText().substring(0, 4));
            } else if (yearTextField.getText().length() == 4 && yearTextField.getText().matches("\\d*")) {
                controller.getEvents(yearTextField.getText());
            } else {
                eventChoiceBox.setDisable(true);
                eventChoiceBox.getSelectionModel().clearSelection();
                controller.clearEvent();
            }

        });

        eventChoiceBox.getSelectionModel().selectedItemProperty().addListener(listener -> {
            if ("Neue Veranstaltung...".equals(eventChoiceBox.getSelectionModel().getSelectedItem())) {
                controller.showNewEventDialog();
                eventChoiceBox.getSelectionModel().clearSelection();
            } else if (eventChoiceBox.getSelectionModel().getSelectedItem() != null) {
                controller.setEvent(eventChoiceBox.getSelectionModel().getSelectedItem());
            }
        });

        addFiles.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            List<File> files = fileChooser.showOpenMultipleDialog(this);
            if (files != null && files.size() > 0) {
                controller.addFiles(files);
            }
        });

        filesList.getSelectionModel().selectedItemProperty().addListener(listener -> {
            if (filesList.getSelectionModel().getSelectedItems().size() > 0) {
                deleteFiles.setDisable(false);
            } else {
                deleteFiles.setDisable(true);
            }
        });

        deleteFiles.setOnAction(event -> controller.deleteFiles(filesList.getSelectionModel().getSelectedItems()));

        resetButton.setOnAction(event -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Alle Felder werden geleert!");
            alert.setTitle("Warnung");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                resetAll();
            }
            event.consume();
        });

        continueButton.setOnAction(event -> {

            String tempuser = userNames.getSelectionModel().getSelectedItem();
            String tempevent = eventChoiceBox.getSelectionModel().getSelectedItem();
            String tempyear = yearTextField.getText();
            String tempdescription = description.getText();

            if (tempuser == null || "".equals(tempuser) || tempevent == null || "".equals(tempevent) || tempyear == null
                    || "".equals(tempyear) || tempdescription == null || "".equals(tempdescription)
                    || filesList.getItems() == null || filesList.getItems().size() <= 0) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Ungültige oder unvollständige Angaben!");
                alert.showAndWait();

            } else {

                reviewVBox.setVisible(true);
                userName.setText(tempuser);
                eventName.setText(tempevent);
                yearName.setText(tempyear.trim());
                descriptionName.setText(tempdescription.trim());
                fileNumber.setText(filesList.getItems().size() + " Dateien");
                fileSize.setText("ca. " + model.getFilesSize() + " MByte");

                controller.setValues(tempuser, tempevent, tempyear, tempdescription);

                BoxBlur bb = new BoxBlur();
                bb.setWidth(5);
                bb.setHeight(5);
                bb.setIterations(3);

                mainVBox.setEffect(bb);
                event.consume();
            }

        });

        cancelButton.setOnAction(event -> {
            reviewVBox.setVisible(false);
            mainVBox.setEffect(null);
            event.consume();
        });

        copyButton.setOnAction(event -> {
            finishButton.setDisable(true);
            reviewVBox.setVisible(false);
            progressVBox.setVisible(true);
            controller.copyFiles();
            event.consume();
        });

        finishButton.setOnAction(event -> {
            progressVBox.setVisible(false);
            resetAll();
        });

    }

    private void createBindings() {
        percent.textProperty().bindBidirectional(progressBar.progressProperty(), new Format() {

            private static final long serialVersionUID = 1L;

            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                Double num = (Double) obj * 100;
                if (num >= 50.0) {
                    percent.setTextFill(Color.WHITE);
                } else {
                    percent.setTextFill(Color.BLACK);
                }
                if (num >= 100.0) {
                    finishButton.setDisable(false);
                }
                return toAppendTo.append(df.format(num)).append("%");
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return Double.parseDouble(source);
            }

        });
        progressBar.progressProperty().bind(model.getProgressProperty());
        copiedFile.textProperty().bind(model.getFileProperty());

    }

    private void setFrameOptions() {
        setTitle("kopy");
        setResizable(true);
        setMinHeight(600);
        setMinWidth(550);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable != null && observable instanceof MainModel) {
            if (arg instanceof String && "events".equals(arg)) {

                eventChoiceBox.setDisable(false);
                List<String> events = new LinkedList<>();
                events.addAll(model.getEvents());
                Collections.sort(events);
                events.add(0, "Neue Veranstaltung...");
                eventChoiceBox.setItems(FXCollections.observableArrayList(events));
                eventChoiceBox.getSelectionModel().select(model.getEvent());

            } else {

                userNames.setItems(FXCollections.observableArrayList(model.getUsers()));
                filesList.setItems(FXCollections.observableArrayList(model.getFilesAsString()));
                userNames.getSelectionModel().select(model.getUser());

            }
        }
    }

}
