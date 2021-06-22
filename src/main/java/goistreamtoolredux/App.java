package goistreamtoolredux;

import goistreamtoolredux.algorithm.CustomTimer;
import goistreamtoolredux.algorithm.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class App extends Application {

    static Logger logger = Logger.getLogger("BWH"); //get logger

    @Override
    public void init() {
        LogCrashHandler.run(); //start up logging
        logger.info("Starting App Initialization");

        logger.info("Verifying Content");
        try {
            FileManager.verifyContent();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Starting App");
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml"));
        primaryStage.setTitle("GoIStreamToolRedux");
        primaryStage.setScene(new Scene(root, 600, 275));
        primaryStage.show();
    }

    @Override
    public void stop() {
        logger.info("Shutting Down");
        //cancel the current TimerTask so the app doesn't hang on quit
        if (CustomTimer.getInstance().getCurrentTimer() != null) {
            CustomTimer.getInstance().getCurrentTimer().cancel();
        }
    }
}

