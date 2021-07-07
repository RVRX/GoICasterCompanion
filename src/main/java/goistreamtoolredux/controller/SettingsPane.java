package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.App;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.InvalidDataException;
import goistreamtoolredux.algorithm.LobbyTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

public class SettingsPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="anchorPane"
    private AnchorPane anchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="inputPathText"
    private Text inputPathText; // Value injected by FXMLLoader

    @FXML // fx:id="outputPathText"
    private Text outputPathText; // Value injected by FXMLLoader

    @FXML // fx:id="osNameText"
    private Text osNameText; // Value injected by FXMLLoader

    @FXML // fx:id="osArchText"
    private Text osArchText; // Value injected by FXMLLoader

    @FXML // fx:id="osVersionText"
    private Text osVersionText; // Value injected by FXMLLoader

    @FXML // fx:id="appVersionText"
    private Text appVersionText; // Value injected by FXMLLoader

    @FXML // fx:id="lobbyTimerSpinner"
    private Spinner<Integer> lobbyTimerSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="themeComboBox"
    private JFXComboBox<String> themeComboBox; // Value injected by FXMLLoader


    void save() {
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Saving..."),new Duration(350)));
        try {
            LobbyTimer.getInstance().setInitialTimerLength(lobbyTimerSpinner.getValue());
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Saved."),new Duration(1000)));

        } catch (IOException exception) {
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Could Not Save","CLOSE",action -> bar.close()), Duration.INDEFINITE, null));
            exception.printStackTrace();
        }
    }


    @FXML
    void openInputFolder(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(FileManager.inputPath));
            } catch (IOException exception) {
                //todo, handle
                exception.printStackTrace();
            }
        }
    }

    @FXML
    void openOutputFolder(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(FileManager.outputPath));
            } catch (IOException exception) {
                //todo, handle
                exception.printStackTrace();
            }
        }
    }

    /**
     * Sets the preferred input folder
     * @param event calling event, passed by JavaFX
     */
    @FXML
    void setPrefInput(ActionEvent event) {
        //setup alert snackbar
        JFXSnackbar bar = new JFXSnackbar(anchorPane);

        //Open Directory Chooser
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Preferred Input Directory");
        chooser.setInitialDirectory(new File(FileManager.getInputPath()));
        File selectedDir = chooser.showDialog(App.getPrimaryStage());
        if (selectedDir == null) {
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("No directory was chosen"),new Duration(1000)));
            return;
        }
        FileManager.setInputPath(selectedDir.getPath() + File.separator);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Input directory updated"),new Duration(1000)));

        //update scrollPane
        inputPathText.setText(FileManager.getInputPath());
    }

    /**
     * Sets the preferred output folder
     * @param event calling event, passed by JavaFX
     */
    @FXML
    void setPrefOutput(ActionEvent event) {
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        //Open Directory Chooser
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Preferred Output Directory");
        chooser.setInitialDirectory(new File(FileManager.getOutputPath()));
        File selectedDir = chooser.showDialog(App.getPrimaryStage());
        if (selectedDir == null) {
            bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("No directory was chosen"),new Duration(1000)));
            return;
        }
        FileManager.setOutputPath(selectedDir.getPath() + File.separator);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Input directory updated"),new Duration(1000)));

        //update scrollPane
        outputPathText.setText(FileManager.getOutputPath());
    }

    @FXML
    void themeComboBoxHandler(ActionEvent event) {
        String item = themeComboBox.getSelectionModel().getSelectedItem();
        App.getMasterController().setTheme(item);
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert anchorPane != null : "fx:id=\"anchorPane\" was not injected: check your FXML file 'SettingsPane.fxml'.";
        assert inputPathText != null : "fx:id=\"inputPathText\" was not injected: check your FXML file 'SettingsPane.fxml'.";
        assert outputPathText != null : "fx:id=\"outputPathText\" was not injected: check your FXML file 'SettingsPane.fxml'.";
        assert osNameText != null : "fx:id=\"osNameText\" was not injected: check your FXML file 'SettingsPane.fxml'.";
        assert osArchText != null : "fx:id=\"osArchText\" was not injected: check your FXML file 'SettingsPane.fxml'.";
        assert osVersionText != null : "fx:id=\"osVersionText\" was not injected: check your FXML file 'SettingsPane.fxml'.";

        inputPathText.setText(FileManager.getInputPath());
        outputPathText.setText(FileManager.getOutputPath());
        osNameText.setText(System.getProperty("os.name"));
        osArchText.setText(System.getProperty("os.arch"));
        osVersionText.setText(System.getProperty("os.version"));
        appVersionText.setText(App.version);

        try {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory
                    (1, Integer.MAX_VALUE, LobbyTimer.getInstance().getInitialTimerLength());
            lobbyTimerSpinner.setValueFactory(valueFactory);
        } catch (IOException | InvalidDataException exception) {
            exception.printStackTrace();
            //todo, handle
        } catch (NoSuchElementException exception) {
            //set initial value to 240, if getInitialTimerLength() throws error
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory
                    (1, Integer.MAX_VALUE, 240);
            lobbyTimerSpinner.setValueFactory(valueFactory);
        }

        ObservableList<String> themeList = FXCollections.observableArrayList();
        themeList.add("Skyborne Light (Default)");
        themeList.add("Skyborne Dark");
        themeList.add("Monochrome Ocean");
        themeComboBox.setItems(themeList);

    }
}