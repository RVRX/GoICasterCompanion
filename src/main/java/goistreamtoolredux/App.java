package goistreamtoolredux;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() {
        System.out.println("Starting Up");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml"));
        primaryStage.setTitle("GoIStreamToolRedux");
        primaryStage.setScene(new Scene(root, 600, 275));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Shutting Down");
    }
}

