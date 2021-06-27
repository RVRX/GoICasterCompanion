package goistreamtoolredux.controller;

import goistreamtoolredux.algorithm.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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

    }
}