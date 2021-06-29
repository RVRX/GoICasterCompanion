package goistreamtoolredux;

import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.LobbyTimer;
import goistreamtoolredux.controller.Master;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    /**The JavaFX application's primary stage. All Scenes are built upon this stage*/
    private static Stage primaryStage;
    private static Master masterController;

    public static Stage getPrimaryStage() { return primaryStage; }

    public static Master getMasterController() {
        return masterController;
    }


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
        App.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Master.fxml"));
        Parent root = loader.load();
        masterController = (Master) loader.getController();
        primaryStage.setTitle("GoIStreamToolRedux");
        primaryStage.setScene(new Scene(root, 700, 400)); // 600 (page) + 100 (sidebar) by 400
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.out.println("Shutting Down");
        //cancel the current TimerTask so the app doesn't hang on quit
        if (LobbyTimer.getInstance().getCurrentTimer() != null) {
            LobbyTimer.getInstance().getCurrentTimer().cancel();
        }
    }
}

