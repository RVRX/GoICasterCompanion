/**
 * Sample Skeleton for 'Master.fxml' Controller Class
 */

package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Master {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

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

        Node sideBar = null;

        try {
            //set page
            sideBar = FXMLLoader.load(getClass().getResource(pathToPage));
            rightVBox.getChildren().set(0, sideBar);
        } catch (IOException e) {
            e.printStackTrace();
            //todo handle
        }
    }
}
