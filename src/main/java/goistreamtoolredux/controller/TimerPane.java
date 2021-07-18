package goistreamtoolredux.controller;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import goistreamtoolredux.App;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.InvalidDataException;
import goistreamtoolredux.algorithm.LobbyTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class TimerPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

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

    @FXML // fx:id="timerToggler"
    private JFXToggleButton timerToggler; // Value injected by FXMLLoader

    @FXML // fx:id="timerEndTextField"
    private JFXTextField timerEndTextField; // Value injected by FXMLLoader

    @FXML // fx:id="timerOneSpinner"
    private Spinner<Integer> timerOneSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="timerTwoSpinner"
    private Spinner<Integer> timerTwoSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private JFXButton saveButton; // Value injected by FXMLLoader

    //preferences
    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");
    private static final String TIMER_ONE_LENGTH = "timer_one_length";
    private static final String TIMER_TWO_LENGTH = "timer_two_length";
    private static final String IS_TIMER_ONE = "is_timer_one";
    private static final String TIMER_END_TEXT = "timer_end_text";



    @FXML
    void lobbyPauseClicked(MouseEvent event) {
        LobbyTimer.getInstance().pause();
    }

    @FXML
    void lobbyPlayClicked(MouseEvent event) {
        try {
            LobbyTimer.getInstance().start();
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo handle
        } catch (InvalidDataException e) {
            e.printStackTrace();
            //todo handle
        }
    }

    @FXML
    void lobbyRestartClicked(MouseEvent event) {
        try {
            LobbyTimer.getInstance().restart();
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo handle
        } catch (InvalidDataException e) {
            e.printStackTrace();
            //todo handle
        }

    }

    @FXML
    void lobbyStopClicked(MouseEvent event) {
        try {
            LobbyTimer.getInstance().stop();
            lobbyTimerText.setText("0");
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo handle
        }
    }

    public void setLobbyTimerText(String value) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                lobbyTimerText.setText(value);
            }
        });
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lobbyPlay != null : "fx:id=\"lobbyPlay\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyPause != null : "fx:id=\"lobbyPause\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyStop != null : "fx:id=\"lobbyStop\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyRestart != null : "fx:id=\"lobbyRestart\" was not injected: check your FXML file 'TimerPane.fxml'.";
        assert lobbyTimerText != null : "fx:id=\"lobbyTimerText\" was not injected: check your FXML file 'TimerPane.fxml'.";

        //get initial lobby timer length
        try {
            lobbyTimerText.setText(String.valueOf(LobbyTimer.getInstance().get()));
        } catch (FileNotFoundException exception) {
            //if file is not found - then there is no current timer, so the default '00:00' is what we want
            // so no changes need to be made
            System.out.println("Timer File not created, 00:00 will be used");
            //BUT, without timer file, timer commands will error out so it must be created
            File timerOut = new File(FileManager.outputPath + "Timer.txt");
            try {
                timerOut.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                //todo, handle failed file creation
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo handle
        }

        //init toggler
        timerToggler.setSelected(!prefs.getBoolean(IS_TIMER_ONE, true));

        //init timer 1 spinner
        initTimerSpinner(timerOneSpinner, prefs.getInt(TIMER_ONE_LENGTH, 240));

        //init timer 2 spinner
        initTimerSpinner(timerTwoSpinner, prefs.getInt(TIMER_TWO_LENGTH, 240));

        //init value for end text TextField
        timerEndTextField.setText(prefs.get(TIMER_END_TEXT, "0:00"));

    }

    static void initTimerSpinner(Spinner<Integer> timerSpinner, int initialLength) {
        timerSpinner.setEditable(true);
        try {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory
                    (1, Integer.MAX_VALUE, initialLength);
            timerSpinner.setValueFactory(valueFactory);

            //set up fix to spinner bug
            timerSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    try {
                        timerSpinner.increment(0); // won't change value, but will commit editor
                    } catch (NumberFormatException exception) {
                        JFXSnackbar bar = new JFXSnackbar(App.getMasterController().getTimerPaneController().anchorPane); //Roundabout way of getting back to this controller through a static method
                        bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Timer Input Invalid","OK",action -> bar.close()),Duration.INDEFINITE));
                    }
                }
            });

        } catch (NoSuchElementException exception) {
            //set initial value to 240, if getInitialTimerLength() throws error
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory
                    (1, Integer.MAX_VALUE, 240);
            timerSpinner.setValueFactory(valueFactory);
        }
    }

    /**
     * Sets the currently active timer preference.
     */
    @FXML
    public void timerTogglerAction(ActionEvent actionEvent) {
        if (timerToggler.isSelected()) {
            //set timerTwo as active
            prefs.putBoolean(IS_TIMER_ONE, false);
        } else {
            //set timer one as active
            prefs.putBoolean(IS_TIMER_ONE, true);
        }
    }

    /**
     * Saves the current changes to the Timer page
     */
    @FXML
    public void save(ActionEvent actionEvent) {
        anchorPane.requestFocus(); //pulls focus away from spinners - allowing them to update their values
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        try {
            LobbyTimer.getInstance().setInitialTimerLength(timerOneSpinner.getValue());
            prefs.putInt(TIMER_ONE_LENGTH, timerOneSpinner.getValue());
            prefs.putInt(TIMER_TWO_LENGTH, timerTwoSpinner.getValue());
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Saving Timers"),new Duration(1000)));
        } catch (IOException exception) {
            //todo
            exception.printStackTrace();
        }

        //update timer end text
        prefs.put(TIMER_END_TEXT, timerEndTextField.getText());
        System.out.println("updating end preference: " + prefs.get(TIMER_END_TEXT, "NOT THERE?"));
    }

}
