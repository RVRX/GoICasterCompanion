package goistreamtoolredux.controller;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TimerPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lobbyPlay"
    private MaterialDesignIconView lobbyPlay; // Value injected by FXMLLoader

    @FXML // fx:id="lobbyPause"
    private MaterialDesignIconView lobbyPause; // Value injected by FXMLLoader

    @FXML // fx:id="lobbyStop"
    private MaterialDesignIconView lobbyStop; // Value injected by FXMLLoader

    @FXML // fx:id="lobbyRestart"
    private MaterialDesignIconView lobbyRestart; // Value injected by FXMLLoader

    @FXML // fx:id="lobbyTimerText"
    private Label lobbyTimerText; // Value injected by FXMLLoader

    @FXML // fx:id="customPlay"
    private MaterialDesignIconView customPlay; // Value injected by FXMLLoader

    @FXML // fx:id="customPause"
    private MaterialDesignIconView customPause; // Value injected by FXMLLoader

    @FXML // fx:id="customStop"
    private MaterialDesignIconView customStop; // Value injected by FXMLLoader

    @FXML // fx:id="customRestart"
    private MaterialDesignIconView customRestart; // Value injected by FXMLLoader

    @FXML // fx:id="customTimerText1"
    private Label customTimerText1; // Value injected by FXMLLoader

    @FXML
    void customPauseClicked(MouseEvent event) {

    }

    @FXML
    void customPlayClicked(MouseEvent event) {

    }

    @FXML
    void customRestartClicked(MouseEvent event) {

    }

    @FXML
    void customStopClicked(MouseEvent event) {

    }

    @FXML
    void lobbyPauseClicked(MouseEvent event) {

    }

    @FXML
    void lobbyPlayClicked(MouseEvent event) {

    }

    @FXML
    void lobbyRestartClicked(MouseEvent event) {

    }

    @FXML
    void lobbyStopClicked(MouseEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lobbyPlay != null : "fx:id=\"lobbyPlay\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyPause != null : "fx:id=\"lobbyPause\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyStop != null : "fx:id=\"lobbyStop\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyRestart != null : "fx:id=\"lobbyRestart\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyTimerText != null : "fx:id=\"lobbyTimerText\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert customPlay != null : "fx:id=\"customPlay\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert customPause != null : "fx:id=\"customPause\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert customStop != null : "fx:id=\"customStop\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert customRestart != null : "fx:id=\"customRestart\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert customTimerText1 != null : "fx:id=\"customTimerText1\" was not injected: check your FXML file 'TimerPane.fxml'.";

    }
}
