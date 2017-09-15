package de.r3r57.kopy.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import de.r3r57.kopy.controller.NewEventController;

public class NewEventView extends Stage {

	private NewEventController controller;
	private Button accept;
	private TextField eventTextField;
	private Label errorLabel;
	private Button cancel;

	public NewEventView(NewEventController controller) {
		this.controller = controller;

		setFrameOptions();

		Pane rootPane = createContentPane();
		initListenerAndEvents();

		rootPane.setStyle("-fx-font-size: 11pt");

		Scene scene = new Scene(rootPane, 350, 85);
		this.setScene(scene);
	}

	private VBox createContentPane() {

		VBox mainVBox = new VBox(15);
		mainVBox.setPadding(new Insets(10, 10, 10, 10));
		mainVBox.setFillWidth(true);

		HBox newUserHBox = new HBox(10);
		newUserHBox.setAlignment(Pos.BASELINE_CENTER);
		HBox buttonsHBox = new HBox(10);
		buttonsHBox.setAlignment(Pos.BASELINE_RIGHT);

		Label userLabel = new Label("Neue Veranstaltung:");
		eventTextField = new TextField("");

		accept = new Button("Hinzufügen");
		cancel = new Button("Abbrechen");
		errorLabel = new Label("");

		newUserHBox.getChildren().addAll(userLabel, eventTextField);
		buttonsHBox.getChildren().addAll(errorLabel, cancel, accept);

		mainVBox.getChildren().addAll(newUserHBox, buttonsHBox);

		return mainVBox;
	}

	private void initListenerAndEvents() {

		eventTextField.textProperty().addListener(listener -> {
			if (eventTextField.getLength() > 30) {
				errorLabel.setText("Name zu lang (max. 30)!");
				accept.setDisable(true);
			} else if (eventTextField.getText().contains("  ") || eventTextField.getText().startsWith(" ")
					|| eventTextField.getText().contains("Neue Veranstaltung")) {
				errorLabel.setText("Ungültiger Name");
				accept.setDisable(true);
			} else {
				errorLabel.setText("");
				accept.setDisable(false);
			}
		});

		accept.setOnAction(event -> {
			if (!"".equals(eventTextField.getText()) && (eventTextField.getLength() <= 30)) {
				controller.addEvent(eventTextField.getText().trim());
				close();
			} else {
				eventTextField.requestFocus();
			}
			event.consume();

		});

		cancel.setOnAction(event -> {
			event.consume();
			close();
		});

	}

	private void setFrameOptions() {
		setTitle("Neue Veranstaltung");
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
	}

}
