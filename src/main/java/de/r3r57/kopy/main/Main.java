package de.r3r57.kopy.main;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import de.r3r57.kopy.controller.MainController;
import de.r3r57.kopy.logger.KopyLogger;
import de.r3r57.kopy.model.MainModel;
import de.r3r57.kopy.view.MainView;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        try {
            new KopyLogger();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FATAL");
            alert.setHeaderText("Unable init Logger!");
            alert.setContentText(e.getMessage());
            alert.show();
        }
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) {

        KopyLogger.logger.info("Starting...");

        MainModel model = new MainModel();
        MainView view = new MainView(model);
        MainController controller = new MainController(view, model);
        
        view.setController(controller);
        view.registerObserver();

    }

}
