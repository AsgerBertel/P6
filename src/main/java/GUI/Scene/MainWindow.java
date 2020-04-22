package GUI.Scene;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

public class MainWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage.setTitle("test");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
