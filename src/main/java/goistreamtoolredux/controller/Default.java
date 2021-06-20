package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class Default {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="TeamAComboBox"
    private JFXComboBox<?> TeamAComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="TeamBComboBox"
    private JFXComboBox<?> TeamBComboBox; // Value injected by FXMLLoader

    @FXML
    void selectTeamA(ActionEvent event) {

    }

    @FXML
    void selectTeamB(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert TeamAComboBox != null : "fx:id=\"TeamAComboBox\" was not injected: check your FXML file 'Default.fxml'.";
        assert TeamBComboBox != null : "fx:id=\"TeamBComboBox\" was not injected: check your FXML file 'Default.fxml'.";
    }
}
