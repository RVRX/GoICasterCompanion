package goistreamtoolredux;

import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import goistreamtoolredux.algorithm.AppTimer;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.InvalidDataException;
import goistreamtoolredux.controller.BindHotkeyPane;
import goistreamtoolredux.controller.Master;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.prefs.Preferences;

import static goistreamtoolredux.controller.BindHotkeyPane.IS_TIMER_ONE;

public class App extends Application {

    //current application version. Used for update checking
    public static final String version = "1.1.0";

    /**The JavaFX application's primary stage. All Scenes are built upon this stage*/
    private static Stage primaryStage;
    private static Master masterController;

    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");

    public static Stage getPrimaryStage() { return primaryStage; }

    public static Master getMasterController() {
        return masterController;
    }


    @Override
    public void init() {
        System.out.println("Starting Up");

        //updates and file verification
        try {
            FileManager.verifyContent();
            FileManager.checkForUpdates();
        } catch (IOException exception) {
            exception.printStackTrace();
            Platform.exit();
        }

        //init hotkeys
//        updateHotKeys();
    }

    /**
     * Clears all global hotkeys, then re-instates, pulling values from preferences.
     */
    public static void updateHotKeys() {
        System.out.println("App.updateHotKeys");
        //clear hotkeys
        Provider provider = Provider.getCurrentProvider(false);
        provider.reset(); //close all keybinds - they will be recreated (if defined) below
        System.out.println("Provider has been reset");

        //global hotkey init
        if (prefs.getBoolean(BindHotkeyPane.IS_HOTKEYS_ENABLED, false)) {
            System.out.println("init hotkeys...");

            //if switch hotkey is set
            String timerSwitchHotKeyString = prefs.get(BindHotkeyPane.HOTKEY_TIMER_SWITCH, null);
            if (timerSwitchHotKeyString != null) {
                //add listener with that keybinding
                provider.register(KeyStroke.getKeyStroke(timerSwitchHotKeyString), getHotKeySwitchListener());
            }

            //if play/pause hotkey is set
            String timerPlayPauseHotKeyString = prefs.get(BindHotkeyPane.HOTKEY_PLAY_PAUSE, null);
            if (timerPlayPauseHotKeyString != null) {
                //add listener with that keybinding
                provider.register(KeyStroke.getKeyStroke(timerPlayPauseHotKeyString), getHotKeyPlayPauseListener());
            }
        }
    }

    /**
     * @return the listener function used for dealing with a timer switch request
     */
    public static HotKeyListener getHotKeySwitchListener() {
        return hotKey -> {
            System.out.println("Timer Swap Hotkey Pressed");
            //swap the current pref
            prefs.putBoolean(IS_TIMER_ONE, !prefs.getBoolean(IS_TIMER_ONE,false));
        };
    }

    /**
     * @return the listener function used for dealing with a play/pause request
     */
    public static HotKeyListener getHotKeyPlayPauseListener() {
        return hotKey -> {
            System.out.println("Timer Play/Pause Hotkey Pressed");
            if (AppTimer.getInstance().isTimerRunning) {
                AppTimer.getInstance().pause();
            } else {
                //try to start timer
                try {
                    AppTimer.getInstance().start();
                } catch (IOException exception) {
                    //todo
                    exception.printStackTrace();
                } catch (InvalidDataException exception) {
                    //todo
                    exception.printStackTrace();
                }
            }
        };
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        App.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Master.fxml"));
        Parent root = loader.load();
        masterController = (Master) loader.getController();
        primaryStage.setTitle("GoICasterCompanion");

        //add icon
        Image icon32 = new Image(getClass().getResourceAsStream("images/splash-cropped-32x32.png"));
        Image icon64 = new Image(getClass().getResourceAsStream("images/splash-cropped-32x32.png"));
        Image icon256 = new Image(getClass().getResourceAsStream("images/splash-cropped-32x32.png"));
        System.out.println("Logos Retrieved");
        primaryStage.getIcons().addAll(icon32, icon64, icon256);
        System.out.println("Icons Added");

        //primary stage settings
        primaryStage.setScene(new Scene(root, 700, 400)); // 600 (page) + 100 (sidebar) by 400
        primaryStage.setOpacity(0);
        primaryStage.setResizable(false);
        primaryStage.show();

        //manual application delay for setup (allow user to disable), and preloader calls
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //notify preloader, so that it may close
                notifyPreloader(new Preloader.ProgressNotification(1));
                //show application stage
                primaryStage.setOpacity(1);
            }
        });
        new Thread(sleeper).start();
    }

    @Override
    public void stop() {
        System.out.println("Shutting Down");
        //cancel the current TimerTask so the app doesn't hang on quit
        if (AppTimer.getInstance().getCurrentTimer() != null) {
            AppTimer.getInstance().getCurrentTimer().cancel();
        }

        //reset the timer file to better align with application UI behaviour on start
        AppTimer timerInstance = AppTimer.getInstance();
        try {
            timerInstance.set(timerInstance.getInitialTimerLength());
        } catch (IOException e) {
            //app is closing, there is not much that can be done here but just let the app close...
            System.err.println("Failed to update timer file on system exit.");
            e.printStackTrace();
        }
    }

    public static void showExceptionDialog(Exception exception, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        System.out.println("posting");
        alert.showAndWait();
    }
}

