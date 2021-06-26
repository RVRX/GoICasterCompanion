package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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


    /**
     *
     * @param keyEvent
     */
    @FXML
    public void keyListener(KeyEvent keyEvent) {
        if (keyEvent.isMetaDown() && keyEvent.getText().equalsIgnoreCase("s")) {
            switch (currentPage) {
                case "/goistreamtoolredux/fxml/TeamPane.fxml":
                    //save team content
                    updateTeams();
                    //snackBar popup, team infos saved
                    JFXSnackbar bar = new JFXSnackbar(masterAnchorPane);
                    bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Teams...",null,null),new Duration(1000)));
                    break;
                case "/goistreamtoolredux/fxml/MapPane.fxml":
                    System.out.println("Map Save Call");
                    //todo save map content
                    break;
            }
        }
    }

    /**
     *
     */
    private void updateTeams() {
        Team teamNameA = TeamPane.getSelectedATeam();
        Team teamNameB = TeamPane.getSelectedBTeam();
        Team teamNameC = TeamPane.getSelectedCTeam();
        Team teamNameD = TeamPane.getSelectedDTeam();
        setTeamIfNotNull(teamNameA, "A");
        setTeamIfNotNull(teamNameB, "B");
        setTeamIfNotNull(teamNameC, "C");
        setTeamIfNotNull(teamNameD, "D");
    }

    /**
     * Helper for {@link #updateTeams()}
     */
    private void setTeamIfNotNull(Team aTeam, String letter) {
        if (aTeam != null) {
            try {
                FileManager.setTeam(aTeam.getTeamName(), "A");
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo, handle
            }
        } else System.out.println("null team");

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
            page = FXMLLoader.load(getClass().getResource(pathToPage));
            rightVBox.getChildren().set(0, page);
            currentPage = pathToPage;
        } catch (IOException e) {
            e.printStackTrace();
            //todo handle
        }
    }
}
