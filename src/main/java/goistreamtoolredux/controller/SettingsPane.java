package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.App;
import goistreamtoolredux.algorithm.CustomTimer;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.InvalidDataException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @FXML // fx:id="lobbyTimerSpinner"
    private Spinner<Integer> lobbyTimerSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="themeComboBox"
    private JFXComboBox<String> themeComboBox; // Value injected by FXMLLoader


    void save() {
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Saving..."),new Duration(350)));
        try {
            CustomTimer.getInstance().setInitialTimerLength(lobbyTimerSpinner.getValue());
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

        try {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory
                    (1, Integer.MAX_VALUE, CustomTimer.getInstance().getInitialTimerLength());
            lobbyTimerSpinner.setValueFactory(valueFactory);
        } catch (IOException | InvalidDataException exception) {
            exception.printStackTrace();
            //todo, handle
        }

        ObservableList<String> themeList = FXCollections.observableArrayList();
        themeList.add("Light");
        themeList.add("Dark");
        themeList.add("Monochrome");
        themeComboBox.setItems(themeList);

    }
}