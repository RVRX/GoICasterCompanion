package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.App;
import goistreamtoolredux.algorithm.FileManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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

    @FXML // fx:id="themeComboBox"
    private JFXComboBox<String> themeComboBox; // Value injected by FXMLLoader

    private static Preferences prefs = Preferences.userRoot().node("/goistreamtoolredux/algorithm");


    void save() { }


    /**Opens the Hotkey Manager*/
    @FXML
    void bindHotKeysAction(ActionEvent event) {
        //todo, open HotKey Manager
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("goistreamtoolredux/fxml/BindHotKeyPane.fxml"), resources);
            Stage stage = new Stage();
            stage.setTitle("HotKey Manager");
            stage.setScene(new Scene(root, 400, 600));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openInputFolder(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(FileManager.inputPath));
            } catch (IOException exception) {
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Failed to Open folder","OK",action -> bar.close()),Duration.INDEFINITE));
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
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.fireEvent(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Failed to Open folder","OK",action -> bar.close()),Duration.INDEFINITE));
                exception.printStackTrace();
            }
        }
    }

    /**
     * Sets the preferred input folder. Prompts app restart
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
        FileManager.refreshPreferences();
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Input directory updated"),new Duration(1000)));

        //update scrollPane
        inputPathText.setText(FileManager.getInputPath());

        //prompt restart
        displayRestartAppDialog();
    }

    /**
     * Sets the preferred output folder. Prompts app restart
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
        FileManager.refreshPreferences();
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Input directory updated"),new Duration(1000)));

        //update scrollPane
        outputPathText.setText(FileManager.getOutputPath());

        //prompt restart
        displayRestartAppDialog();
    }

    @FXML
    void themeComboBoxHandler(ActionEvent event) {
        String item = themeComboBox.getSelectionModel().getSelectedItem();
        prefs.put(Master.PREFERRED_THEME, item);
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

        ObservableList<String> themeList = FXCollections.observableArrayList();
        themeList.add("Skyborne Light (Default)");
        themeList.add("Skyborne Dark");
        themeList.add("Monochrome Ocean [Legacy]");
        themeComboBox.setItems(themeList);

    }

    /**
     * Prompts the user if they would like to reset preferences.
     * Calls {@link FileManager#resetPreferences()} if the user selects 'OK'.
     * Will prompt user to restart app afterwards.
     * @param actionEvent calling event's action details, typically handled by FXML
     */
    @FXML
    public void resetPreferencesAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Reset App Preferences");
        alert.setContentText("Are you sure you want to do this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            try {
                //reset
                FileManager.resetPreferences();
                //refresh prefs (alternative to a restart)
                FileManager.refreshPreferences();
                //prompt user for restart
                displayRestartAppDialog();
            } catch (BackingStoreException e) {
                e.printStackTrace();
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error Dialog");
                error.setHeaderText("Could Not Reset Preferences");
                error.setContentText("An error occurred when attempting to reset the preferences to default");
                error.showAndWait();
            }
        }
    }

    /**
     * Displays a prompt to the user asking them to restart the program for their changes to take effect.
     * If the user selects 'OK' the program will initiate shutdown.
     */
    private void displayRestartAppDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Restart Dialog");
        alert.setHeaderText("It is recommended that the application be restarted after performing this action.");
        alert.setContentText("Press 'OK' to close the app now (you will have to restart manually)");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
        }
    }
}