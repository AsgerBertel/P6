package GUI.Scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root =  FXMLLoader.load(getClass().getResource("/FXML/MainScene.fxml"));
        stage.setTitle("P6");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
}
