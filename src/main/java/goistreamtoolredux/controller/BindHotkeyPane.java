package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import goistreamtoolredux.algorithm.AppTimer;
import goistreamtoolredux.algorithm.InvalidDataException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

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

    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
    private static final String IS_TIMER_ONE = "is_timer_one";
    private static final String IS_HOTKEYS_ENABLED = "is_hotkeys_enabled";
    private static final String HOTKEY_PLAY_PAUSE = "hotkey_play_pause";
    private static final String HOTKEY_TIMER_SWITCH = "hotkey_timer_switch";


    @FXML
    void enableHotkeyTogglerAction(ActionEvent event) {
        //put status of toggler into preference
        prefs.putBoolean(IS_HOTKEYS_ENABLED, enableHotkeyToggle.isSelected());

        //enable or disable fields
        timerPlayPauseField.setEditable(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
        timerSwitchField.setEditable(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
    }

    @FXML
    void timerPlayPauseFieldAction(ActionEvent event) {
        if (timerPlayPauseField.getText() == null || timerPlayPauseField.getText().equals("")) {
            return;
        }

        //check input keystroke viability
        KeyStroke inputKeyStroke = KeyStroke.getKeyStroke(timerPlayPauseField.getText());
        if (inputKeyStroke == null) {
            //todo, popup
            timerPlayPauseField.setText("");
            return;
        }

        //define listener action
        HotKeyListener listener = hotKey -> {
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

        //set listener
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(inputKeyStroke, listener);
    }

    @FXML
    void timerSwitchFieldAction(ActionEvent event) {
        if (timerSwitchField.getText() == null || timerSwitchField.getText().equals("")) {
            return;
        }

        //check input keystroke viability
        KeyStroke inputKeyStroke = KeyStroke.getKeyStroke(timerSwitchField.getText());
        if (inputKeyStroke == null) {
            //todo, popup
            timerSwitchField.setText("");
            return;
        }

        //define listener action
        HotKeyListener listener = hotKey -> {
            System.out.println("Timer Swap Hotkey Pressed");
            //swap the current pref
            prefs.putBoolean(IS_TIMER_ONE, !prefs.getBoolean(IS_TIMER_ONE,false));
        };

        //set listener
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(inputKeyStroke, listener);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert timerPlayPauseField != null : "fx:id=\"timerPlayPauseField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert enableHotkeyToggle != null : "fx:id=\"enableHotkeyToggle\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert timerSwitchField != null : "fx:id=\"timerSwitchField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";

        //apply default app theme
        anchorPane.getStylesheets().setAll("/goistreamtoolredux/css/light/MainStyle.css");

        //init toggler
        enableHotkeyToggle.setSelected(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));

        //init field editable status
        timerPlayPauseField.setEditable(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
        timerSwitchField.setEditable(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
    }
}
