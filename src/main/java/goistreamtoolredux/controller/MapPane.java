package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.algorithm.FileManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
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

    @FXML // fx:id="mapImage"
    private ImageView mapImage; // Value injected by FXMLLoader

    @FXML // fx:id="mapDetailText"
    private Text mapDetailText; // Value injected by FXMLLoader

    private String selectedMap;

    @FXML
    void selectMap(ActionEvent event) {
        selectedMap = mapComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        File mapFile = new File(FileManager.inputPath + "map_images" + File.separator + selectedMap + ".png");
        mapImage.setImage(new Image(mapFile.toURI().toString()));
        //todo update text, current is concept filler
        mapDetailText.setText("Selected Map is " + selectedMap);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mapComboBox != null : "fx:id=\"mapComboBox\" was not injected: check your FXML file 'MapPane.fxml'.";
        assert mapImage != null : "fx:id=\"mapImage\" was not injected: check your FXML file 'MapPane.fxml'.";
        assert mapDetailText != null : "fx:id=\"mapDetailText\" was not injected: check your FXML file 'MapPane.fxml'.";

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
                FileManager.setMap(selectedMap);
                //snackBar popup, team infos saved
                JFXSnackbar bar = new JFXSnackbar(anchorPane);
                bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Map...",null,null),new Duration(1000)));
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo, error popup
            }
        }
    }
}
