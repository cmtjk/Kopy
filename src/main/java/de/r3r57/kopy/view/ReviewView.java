package de.r3r57.kopy.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.r3r57.kopy.controller.MainController;

public class ReviewView extends Stage {

	private MainController controller;
	private String user;
	private String event;
	private String year;
	private String description;
	private int files;
	private int filesSize;

	public ReviewView(MainController controller, String user, String event, String year, String description, int files,
			int filesSize) {
		this.controller = controller;

		this.user = user;
		this.event = event;
		this.year = year;
		this.description = description;
		this.files = files;
		this.filesSize = filesSize;

		setFrameOptions();

		Pane rootPane = createContentPane();
		initListenerAndEvents();

		rootPane.setStyle("-fx-font-size: 11pt");

		Scene scene = new Scene(rootPane, 400, 300);
		this.setScene(scene);

	}

	private VBox createContentPane() {

		VBox reviewPane = new VBox(10);
		reviewPane.setPadding(new Insets(10, 10, 10, 10));

		GridPane reviewGrid = new GridPane();

		reviewGrid.setHgap(4);
		reviewGrid.setVgap(6);
		// reviewGrid.setPadding(new Insets(15, 15, 15, 15));
		reviewGrid.setPrefHeight(250);
		reviewGrid.setPrefWidth(500);

		Label userLabel = new Label("Nutzer:");
		Label userName = new Label(user);
		Label eventLabel = new Label("Veranstaltung:");
		Label eventName = new Label(event);
		Label yearLabel = new Label("Jahr:");
		Label yearName = new Label(year);
		Label fileNumber = new Label(files + " Dateien");
		Label fileSize = new Label("ca." + filesSize + " MByte");

		userName.setStyle("-fx-font-weight:bold;");
		eventName.setStyle("-fx-font-weight:bold;");
		yearName.setStyle("-fx-font-weight:bold;");

		Label descriptionLabel = new Label("Beschreibung:");
		TextArea descriptionName = new TextArea(description);
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
		GridPane.setConstraints(descriptionLabel, 1, 4);
		GridPane.setConstraints(descriptionName, 1, 5, 5, 5);
		GridPane.setConstraints(fileNumber, 1, 10);
		GridPane.setConstraints(fileSize, 3, 10);

		reviewGrid.getChildren().addAll(userLabel, userName, eventLabel, eventName, yearLabel, yearName,
				descriptionLabel, descriptionName, fileNumber, fileSize);

		Button copy = new Button("Übertragen");
		Button cancel = new Button("Abbrechen");

		HBox buttonHBox = new HBox(25);
		buttonHBox.setAlignment(Pos.BASELINE_RIGHT);
		buttonHBox.getChildren().addAll(cancel, copy);

		reviewPane.getChildren().addAll(reviewGrid, buttonHBox);

		return reviewPane;
	}

	private void initListenerAndEvents() {

	}

	private void setFrameOptions() {

		setTitle("Überprüfen");
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
	}

}
