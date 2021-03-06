package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Master {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="masterAnchorPane"
    private AnchorPane masterAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="navigationSidebarVBox"
    private VBox navigationSidebarVBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamButton"
    private JFXButton teamButton; // Value injected by FXMLLoader

    @FXML // fx:id="mapButton"
    private JFXButton mapButton; // Value injected by FXMLLoader

    @FXML // fx:id="timerButton"
    private JFXButton timerButton; // Value injected by FXMLLoader

    @FXML // fx:id="tournamentButton"
    private JFXButton tournamentButton; // Value injected by FXMLLoader

    @FXML // fx:id="settingsButton"
    private JFXButton settingsButton; // Value injected by FXMLLoader

    @FXML // fx:id="rightVBox"
    private VBox rightVBox; // Value injected by FXMLLoader


    //---VARIABLES---
    private String currentPage;
    /** References to controllers, set within {@link #setPage(String)}
     * Just because a controller variable here is not null, does not mean it is valid.
     * It is only valid while {@link #currentPage} is equal to the path to that controller's
     * FXML loader.
     *
     * These variables allow for communication with controller objects. ie, calling non-static functions.
     * */
    private TeamPane teamPaneController;
    private MapPane mapPaneController;
    private TimerPane timerPaneController;
    private TournamentPane tournamentPaneController;
    private SettingsPane settingsPaneController;

    //prefs
    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
    public static final String PREFERRED_THEME = "default_theme";

    public TimerPane getTimerPaneController() {
        return timerPaneController;
    }


    /**
     * Upon hearing a <code>META+S</code> keypress, throws a <code>save()</code> call to
     * the currently active controller for the right side (main) content.
     * @param keyEvent User's keypress
     */
    @FXML
    public void keyListener(KeyEvent keyEvent) {
        if ((keyEvent.isMetaDown() && keyEvent.getText().equalsIgnoreCase("s")) || (keyEvent.isControlDown() && keyEvent.getText().equalsIgnoreCase("s"))) {
            switch (currentPage) {
                case "/goistreamtoolredux/fxml/TeamPane.fxml":
                    //save team content
                    teamPaneController.save();
                    break;
                case "/goistreamtoolredux/fxml/MapPane.fxml":
                    mapPaneController.save();
                    break;
                case "/goistreamtoolredux/fxml/TournamentPane.fxml":
                    tournamentPaneController.saveChanges(null);
                    break;
                case "/goistreamtoolredux/fxml/SettingsPane.fxml":
                    settingsPaneController.save();
                    break;
                case "/goistreamtoolredux/fxml/TimerPane.fxml":
                    timerPaneController.save(null);
                    break;
            }
        }
        else if ((keyEvent.isMetaDown() && keyEvent.getText().equalsIgnoreCase("w")) || (keyEvent.isControlDown() && keyEvent.getText().equalsIgnoreCase("w"))) {
            switch (currentPage) {
                case "/goistreamtoolredux/fxml/TeamPane.fxml":
                    //save team content
                    teamPaneController.clear();
                    break;
                case "/goistreamtoolredux/fxml/MapPane.fxml":
                    mapPaneController.clear();
                    break;
            }
        }
    }

    @FXML
    void teamButtonAction(ActionEvent event) {
        setPage("/goistreamtoolredux/fxml/TeamPane.fxml");
    }

    @FXML
    void mapButtonAction(ActionEvent event) {
        setPage("/goistreamtoolredux/fxml/MapPane.fxml");
    }

    @FXML
    void settingsButtonAction(ActionEvent event) {
        setPage("/goistreamtoolredux/fxml/SettingsPane.fxml");
    }

    @FXML
    void timerButtonAction(ActionEvent event) {
        setPage("/goistreamtoolredux/fxml/TimerPane.fxml");
    }

    @FXML
    void tournamentButtonAction(ActionEvent event) {
        setPage("/goistreamtoolredux/fxml/TournamentPane.fxml");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'Master.fxml'.";

        //add Loading label to child 0
        rightVBox.getChildren().add(0,new Label("Loading.."));

        //update child 0 to Default Page (Teams)
        setPage("/goistreamtoolredux/fxml/TeamPane.fxml");

        //set default theme
        setTheme(prefs.get(PREFERRED_THEME, "skyborne light (default)"));
    }

    /**
     * Sets the current page/pane seen on the right of the app.
     * Should typically be triggered by a button press in the navigation menu.
     *
     * @implNote rightVBox must have a child node at index 0.
     * @param pathToPage full package style path from project root to FXML file
     */
    private void setPage(String pathToPage) {
        System.out.println("setPage(" + pathToPage + ")");

        Node page = null;

        try {
            //set page
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToPage));
            Parent root = loader.load();
            rightVBox.getChildren().set(0, root);

            //save currentPage string, for purposes of finding
            // current controller variable, as set in below switch.
            currentPage = pathToPage;

            switch (pathToPage) { //save reference to correct controller variable
                case "/goistreamtoolredux/fxml/TeamPane.fxml":
                    teamPaneController = (TeamPane) loader.getController();
                    break;
                case "/goistreamtoolredux/fxml/MapPane.fxml":
                    mapPaneController = (MapPane) loader.getController();
                    break;
                case "/goistreamtoolredux/fxml/TimerPane.fxml":
                    timerPaneController = (TimerPane) loader.getController();
                    break;
                case "/goistreamtoolredux/fxml/TournamentPane.fxml":
                    tournamentPaneController = (TournamentPane) loader.getController();
                    break;
                case "/goistreamtoolredux/fxml/SettingsPane.fxml":
                    settingsPaneController = (SettingsPane) loader.getController();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            //todo handle
        }
    }


    /**
     * Sets the current application theme
     * @param name one of the theme names
     */
    public void setTheme(String name) {
        System.out.println("name: " + name);
        if (name.equalsIgnoreCase("skyborne dark")) {
             masterAnchorPane.getStylesheets().setAll("/goistreamtoolredux/css/dark/MainStyle.css");
             navigationSidebarVBox.getStylesheets().setAll("/goistreamtoolredux/css/dark/Navigation.css");
         }
         else if (name.equalsIgnoreCase("Skyborne Light (Default)")) {
             masterAnchorPane.getStylesheets().setAll("/goistreamtoolredux/css/light/MainStyle.css");
             navigationSidebarVBox.getStylesheets().setAll("/goistreamtoolredux/css/light/Navigation.css");
         } else if (name.equalsIgnoreCase("monochrome ocean [legacy]")) {
             masterAnchorPane.getStylesheets().setAll("/goistreamtoolredux/css/monochrome/MainStyle.css");
             navigationSidebarVBox.getStylesheets().setAll("/goistreamtoolredux/css/monochrome/Navigation.css");
        }
    }

    public static void newWarning(String title, String header, String content) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
            }
        });
    }
}
