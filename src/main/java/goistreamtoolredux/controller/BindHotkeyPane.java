package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class BindHotkeyPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="timerPlayPauseField"
    private JFXTextField timerPlayPauseField; // Value injected by FXMLLoader

    @FXML // fx:id="enableHotkeyToggle"
    private JFXToggleButton enableHotkeyToggle; // Value injected by FXMLLoader

    @FXML // fx:id="timerSwitchField"
    private JFXTextField timerSwitchField; // Value injected by FXMLLoader

    @FXML
    void enableHotkeyAction(ActionEvent event) {

    }

    @FXML
    void timerPlayPauseFieldAction(ActionEvent event) {

    }

    @FXML
    void timerSwitchFieldAction(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert timerPlayPauseField != null : "fx:id=\"timerPlayPauseField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert enableHotkeyToggle != null : "fx:id=\"enableHotkeyToggle\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert timerSwitchField != null : "fx:id=\"timerSwitchField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";

        //apply default app theme
        anchorPane.getStylesheets().setAll("/goistreamtoolredux/css/light/MainStyle.css");
    }
}
