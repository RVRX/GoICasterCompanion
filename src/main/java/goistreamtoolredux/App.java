package goistreamtoolredux;

import goistreamtoolredux.algorithm.CustomTimer;
import goistreamtoolredux.algorithm.FileManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() {
        System.out.println("Starting Up");
        try {
            FileManager.verifyContent();
        } catch (IOException exception) {
            exception.printStackTrace();
            Platform.exit();
        }
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
        //cancel the current TimerTask so the app doesn't hang on quit
        if (CustomTimer.getInstance().getCurrentTimer() != null) {
            CustomTimer.getInstance().getCurrentTimer().cancel();
        }
    }
}

