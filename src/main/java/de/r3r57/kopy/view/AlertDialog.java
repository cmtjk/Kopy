package de.r3r57.kopy.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertDialog extends Alert {

	private AlertDialog() {
		super(AlertType.ERROR);
	}

	public static void showErrorDialog(String message, String details, Exception e) {

		Platform.runLater(() -> {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date date = new Date();

			AlertDialog alert = new AlertDialog();

			alert.setTitle("Error");
			alert.setHeaderText(message);
			alert.setContentText(details);

			System.out.println(dateFormat.format(date.getTime()) + " " + e);

			alert.showAndWait();

		});

	}

}
