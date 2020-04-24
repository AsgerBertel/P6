package GUI.Scene;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class MainWindow extends Application {
    private static final int MIN_WIDTH = 1200;
    private static final int MIN_HEIGHT = 800;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("/FXML/MainScene.fxml"));
        primaryStage.setTitle("P6");
        Scene scene = new Scene(root);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
