package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.algorithm.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MapPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    public AnchorPane anchorPane;

    @FXML // fx:id="mapComboBox"
    private JFXComboBox<String> mapComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="showSpawnsCheckBox"
    private JFXCheckBox showSpawnsCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="mapImage"
    private ImageView mapImage; // Value injected by FXMLLoader

    private String selectedMap;

    @FXML
    void selectMap(ActionEvent event) {
        selectedMap = mapComboBox.getSelectionModel().getSelectedItem();
        if (selectedMap != null) {
            //update UI image
            File mapFile;
            if (showSpawnsCheckBox.isSelected()) {
                //show spawn image
                mapFile = new File(FileManager.inputPath + "map_images" + File.separator + selectedMap + " Spawn.png");
                if (!mapFile.exists()) { //if spawn image can't be found, use non-spawn variant as fallback
                    mapFile = new File(FileManager.inputPath + "map_images" + File.separator + selectedMap + ".png");
                }
            } else {
                //show normal image
                mapFile = new File(FileManager.inputPath + "map_images" + File.separator + selectedMap + ".png");
            }
            mapImage.setImage(new Image(mapFile.toURI().toString()));
        } else {
            mapImage.setImage(null);
        }
    }

    @FXML
    void spawnStatusChanged(ActionEvent event) {
        selectMap(event);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mapComboBox != null : "fx:id=\"mapComboBox\" was not injected: check your FXML file 'MapPane.fxml'.";
        assert mapImage != null : "fx:id=\"mapImage\" was not injected: check your FXML file 'MapPane.fxml'.";

        try {
            //init map combo box
            ObservableList<String> mapObsList = FXCollections.observableArrayList();
            LinkedList<String> mapLL = FileManager.getAllMapsFromDisk();
            Collections.sort(mapLL);
            mapObsList.addAll(mapLL);
            mapComboBox.setItems(mapObsList);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        //save map
        if (selectedMap != null) {
            try {
                System.out.println("selected map: " + selectedMap);
                System.out.println("checkbox: " + showSpawnsCheckBox.isSelected());
                FileManager.setMap(selectedMap, showSpawnsCheckBox.isSelected());
                //snackBar popup, team infos saved
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Map...",null,null),new Duration(1000)));
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Error Finding Map File...",null,null),new Duration(1000)));
            } catch (IOException exception) {
                exception.printStackTrace();
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Error Setting Map...",null,null),new Duration(1000)));
            }
        } else { //No map was selected
            //Delete Map file
            try {
                FileManager.removeMap();
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo handle
            }
        }
    }

    /**
     * Clears the current map (In UI and filesystem).
     */
    public void clear() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Map files will be cleared");
        alert.setContentText("This action does not need saving, and will be executed immediately. Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            //Clear UI
            mapComboBox.getSelectionModel().clearSelection();

            //Clear Map File and Map Text
            save();
        }
    }
}
