package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import javax.swing.*;
import java.net.URL;
import java.util.LinkedList;
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
    public static final String IS_TIMER_ONE = "is_timer_one";
    public static final String IS_HOTKEYS_ENABLED = "is_hotkeys_enabled";
    public static final String HOTKEY_PLAY_PAUSE = "hotkey_play_pause";
    public static final String HOTKEY_TIMER_SWITCH = "hotkey_timer_switch";

    private LinkedList<KeyEvent> playPauseHotKey = new LinkedList<>();
    private LinkedList<String> foo = new LinkedList<String>();
    private String hotkeyString = "";


    @FXML
    void enableHotkeyTogglerAction(ActionEvent event) {
        //put status of toggler into preference
        prefs.putBoolean(IS_HOTKEYS_ENABLED, enableHotkeyToggle.isSelected());

        //enable or disable fields
        timerPlayPauseField.setDisable(!prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
        timerSwitchField.setDisable(!prefs.getBoolean(IS_HOTKEYS_ENABLED, false));

        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout("Updated HotKey live status. Restart to apply change", "OK", action -> bar.close()),
                Duration.millis(2500), null));
    }

    /**
     * todo
     *
     * Called by FXML/JavaFX thread when 'enter' key is pressed
     * while the {@link #timerPlayPauseField} is focused.
     * @param event
     */
    @FXML
    void timerPlayPauseFieldAction(ActionEvent event) {
        //update prefs
        getKeyStrokeAndSaveToPrefs(timerPlayPauseField, HOTKEY_PLAY_PAUSE);
    }

    /**
     * todo
     *
     * Called by FXML/JavaFX thread when 'enter' key is pressed
     * while the {@link #timerSwitchField} is focused.
     * @param event
     */
    @FXML
    void timerSwitchFieldAction(ActionEvent event) {
        System.out.println("putting '" + hotkeyString + "' into timer switch preference");
        prefs.put(HOTKEY_TIMER_SWITCH, hotkeyString);
        getKeyStrokeAndSaveToPrefs(timerSwitchField, HOTKEY_TIMER_SWITCH);
        //todo prompt restart!
    }

    /**
     * todo
     * Adds the pressed key - if not 'enter' key - into a linkedList of keyEvents.
     *
     * Called by FXML/JavaFX thread when any key is pressed
     * while the {@link #timerPlayPauseField} is focused.
     * @param event
     */
    @FXML
    void playPauseKeyPressed(KeyEvent event) {
//        if (prefs.getBoolean(IS_HOTKEYS_ENABLED, false)) {
//            System.out.println(event);
//            foo.add(event.getCode());
//            playPauseHotKey.add(event);
//        }
    }

    /**
     * todo
     * Adds the pressed key - if not 'enter' key - into a linkedList of keyEvents.
     *
     * Called by FXML/JavaFX thread when any key is pressed
     * while the {@link #timerPlayPauseField} is focused.
     * @param event
     */
    @FXML
    void switchKeyPressed(KeyEvent event) {
        //todo KeyEvent to KeyStroke
        KeyCode pressedKeyCode = event.getCode();
        KeyStroke.getAWTKeyStrokeForEvent(event);
        KeyCodeCombination.keyCombination(event.getCode().toString());

        if (!pressedKeyCode.equals(KeyCode.ENTER)) { //if not 'enter' key
            if (!pressedKeyCode.equals(KeyCode.BACK_SPACE)) {
                System.out.println("NOT ENTER");
                System.out.println(event);
//                event

                //add to list of keys
//            String currentHotkeyString = prefs.get(HOTKEY_TIMER_SWITCH, "");
//            String newHotkeyString = currentHotkeyString + " " + event.getCode().toString();
//            prefs.put(HOTKEY_TIMER_SWITCH, newHotkeyString);
//                hotkeyString += " " + event.getCode()
                if (event.getCode().equals(KeyCode.SHIFT)) {
                    hotkeyString += " " + "SHIFT";
                    hotkeyString += " " + event.getCode().toString();
                } else if (event.getCode().equals(KeyCode.ALPHANUMERIC)) {

                }
                hotkeyString += " " + event.getCode().toString();
//            foo.add(event.getCode().toString());

//            foo.add(event.getCode());

                //add to field
                timerSwitchField.setText(timerSwitchField.getText() + " " + event.getCode().toString());

            } else { //key pressed is backspace
                hotkeyString = "";
                timerSwitchField.setText("");
            }
        } else  System.err.println(event);
    }

    /**
     * Gets string from textField, and if its a valid {@link KeyStroke}, saves it to provided preference.
     * Remove preference if field is blank
     * @param textField Location to pull string from to test parsing into {@link KeyStroke}, and pass to preference
     * @param preferenceKey preference key save textField string into
     * @return either null if no preference was set due to invalid text, or the KeyStroke if valid.
     */
    private KeyStroke getKeyStrokeAndSaveToPrefs(JFXTextField textField, String preferenceKey) {
        if (textField.getText() == null || textField.getText().equals("")) {
            prefs.remove(preferenceKey); //remove preference value
            return null;
        }

        //check input keystroke viability
        KeyStroke inputKeyStroke = KeyStroke.getKeyStroke(textField.getText());
        if (inputKeyStroke == null) {
            //keystroke invalid, clear field
            textField.setText("");
            return null;
        }
        //save keystroke to prefs
        prefs.put(preferenceKey, textField.getText());
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout("Updated '" + preferenceKey + "'. Restart to apply changes", "OK", action -> bar.close()),
                Duration.INDEFINITE, null));
        return inputKeyStroke;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert timerPlayPauseField != null : "fx:id=\"timerPlayPauseField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert enableHotkeyToggle != null : "fx:id=\"enableHotkeyToggle\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";
        assert timerSwitchField != null : "fx:id=\"timerSwitchField\" was not injected: check your FXML file 'BindHotkeyPane.fxml'.";

        //apply default app theme
        anchorPane.getStylesheets().setAll("/goistreamtoolredux/css/light/MainStyle.css");

        //init toggler value
        enableHotkeyToggle.setSelected(prefs.getBoolean(IS_HOTKEYS_ENABLED, false));

        //init field disable status
        timerPlayPauseField.setDisable(!prefs.getBoolean(IS_HOTKEYS_ENABLED, false));
        timerSwitchField.setDisable(!prefs.getBoolean(IS_HOTKEYS_ENABLED, false));

        //dont allow direct typing into field
        timerSwitchField.setEditable(false);
        timerPlayPauseField.setEditable(false);

        //init field values
        timerPlayPauseField.setText(prefs.get(HOTKEY_PLAY_PAUSE, null));
        timerSwitchField.setText(prefs.get(HOTKEY_TIMER_SWITCH, null));

        //init key thingy todo
    }
}
