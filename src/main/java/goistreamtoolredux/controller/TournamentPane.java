package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.algorithm.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TournamentPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="tournamentNumberSpinner"
    private Spinner<?> tournamentNumberSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private JFXButton saveButton; // Value injected by FXMLLoader

    @FXML
    void saveChanges(ActionEvent event) {
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Teams...",null,null),new Duration(1000)));
        FileManager.setTourneyNumber(tournamentNumberSpinner.getValue().toString());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tournamentNumberSpinner != null : "fx:id=\"tournamentNumberSpinner\" was not injected: check your FXML file 'TournamentPane.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'TournamentPane.fxml'.";

    }
}