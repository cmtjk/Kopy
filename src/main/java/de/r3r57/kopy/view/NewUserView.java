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
import de.r3r57.kopy.controller.NewUserController;

public class NewUserView extends Stage {

	private NewUserController controller;
	private Button accept;
	private TextField userTextField;
	private Label errorLabel;
	private Button cancel;

	public NewUserView(NewUserController controller) {
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

		Label userLabel = new Label("Neuer Nutzer:");
		userTextField = new TextField("");

		accept = new Button("Hinzufügen");
		cancel = new Button("Abbrechen");
		errorLabel = new Label("");

		newUserHBox.getChildren().addAll(userLabel, userTextField);
		buttonsHBox.getChildren().addAll(errorLabel, cancel, accept);

		mainVBox.getChildren().addAll(newUserHBox, buttonsHBox);

		return mainVBox;
	}

	private void initListenerAndEvents() {

		userTextField.textProperty().addListener(listener -> {
			if (userTextField.getLength() > 30) {
				errorLabel.setText("Zu lang (max 30)!");
				accept.setDisable(true);
			} else if (userTextField.getText().contains("  ") || userTextField.getText().startsWith(" ")
					|| userTextField.getText().contains("Neuer Nutzer") || userTextField.getText().contains(".")
					|| userTextField.getText().contains("/") || userTextField.getText().contains("\\")) {
				errorLabel.setText("Ungültiger Nutzername!");
				accept.setDisable(true);
			} else {
				errorLabel.setText("");
				accept.setDisable(false);
			}
		});

		accept.setOnAction(event ->

		{
			if (!"".equals(userTextField.getText()) && (userTextField.getLength() <= 30)) {
				controller.newUser(userTextField.getText().trim());
				close();
			} else {
				userTextField.requestFocus();
			}
			event.consume();

		});

		cancel.setOnAction(event ->

		{
			event.consume();
			close();
		});

	}

	private void setFrameOptions() {
		setTitle("Neuer Nutzer");
		setResizable(false);
		initModality(Modality.APPLICATION_MODAL);
	}

}
