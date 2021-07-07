package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import goistreamtoolredux.algorithm.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class TournamentPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="tournamentNumberSpinner"
    private Spinner<Integer> tournamentNumberSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private JFXButton saveButton; // Value injected by FXMLLoader

    @FXML // fx:id="tournamentNameField"
    private JFXTextField tournamentNameField; // Value injected by FXMLLoader

    @FXML
    void saveChanges(ActionEvent event) {
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Tournament Info...",null,null),new Duration(500)));
        FileManager.setTourneyNumber(tournamentNumberSpinner.getValue().toString());
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updated",null,null),new Duration(1000)));
        //save tournamentName to file
        try {
            FileManager.setTournamentName(tournamentNameField.getText());
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo, handle
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tournamentNumberSpinner != null : "fx:id=\"tournamentNumberSpinner\" was not injected: check your FXML file 'TournamentPane.fxml'.";
        assert saveButton != null : "fx:id=\"saveButton\" was not injected: check your FXML file 'TournamentPane.fxml'.";

        //inits spinner to tournament number found in TournamentNumber.txt, or 1 if the file contained unexpected contents.
        int savedTournamentNumber = 1;
        try {
            savedTournamentNumber = FileManager.getTourneyNumber();
        } catch (NumberFormatException exception) {
            Master.newWarning("File Error", "Error reading TournamentNumber.txt","Tournament file did not contain an expected value");
        } catch (FileNotFoundException exception) {
            //file was not found, will be created upon user saving input [saveChanges() call]. No need to handle.
            System.out.println("Tournament File not found. Defaulting to 1");
        }
        tournamentNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, savedTournamentNumber));

        //set tournament text field to current in file
        try {
            tournamentNameField.setText(FileManager.getTournamentName());
        } catch (FileNotFoundException exception) {
            //file has not yet been created
            //but will be crated on save
        } catch (NoSuchElementException exception) {
            //file is empty,
            //but contents will be overwritten on save so
            //nothing to worry about here
        }
    }
}