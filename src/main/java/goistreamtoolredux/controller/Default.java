package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Default {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="TeamAComboBox"
    private JFXComboBox<Team> TeamAComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="TeamBComboBox"
    private JFXComboBox<Team> TeamBComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="saveButton"
    private JFXButton saveButton; // Value injected by FXMLLoader

    @FXML // fx:id="teamAImage"
    private ImageView teamAImage = new ImageView(); // Value injected by FXMLLoader

    @FXML // fx:id="mapImage"
    private ImageView mapImage; // Value injected by FXMLLoader

    @FXML // fx:id="teamBImage"
    private ImageView teamBImage; // Value injected by FXMLLoader

    private Team selectedATeam;
    private Team selectedBTeam;

    /**
     * Called by the "save" button. Intended to save/set all of the currently selected options
     * @param event
     */
    @FXML
    void save(ActionEvent event) {
        // save team selections
        if (selectedATeam != null) { //A Team
            try {
                FileManager.setTeam(selectedATeam.getTeamName(), "A");
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo, error popup
            }
        }
        if (selectedBTeam != null) { //B Team
            try {
                FileManager.setTeam(selectedBTeam.getTeamName(), "B");
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo, error popup
            }
        }
    }

    @FXML
    void selectTeamA(ActionEvent event) {
        selectedATeam = TeamAComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedATeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamAImage.setImage(null);
        } else {
            //update image
            teamAImage.setImage(new Image(selectedATeam.getTeamLogo().toUri().toString()));
        }
    }

    @FXML
    void selectTeamB(ActionEvent event) {
        selectedBTeam = TeamBComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedBTeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamBImage.setImage(null);
        } else {
            //update image
            teamBImage.setImage(new Image(selectedBTeam.getTeamLogo().toUri().toString()));
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert TeamAComboBox != null : "fx:id=\"TeamAComboBox\" was not injected: check your FXML file 'Default.fxml'.";
        assert TeamBComboBox != null : "fx:id=\"TeamBComboBox\" was not injected: check your FXML file 'Default.fxml'.";

        LinkedList<ComboBox<Team>> comboBoxesToInit = new LinkedList<>();
        comboBoxesToInit.add(TeamAComboBox);
        comboBoxesToInit.add(TeamBComboBox);
        try {
            teamComboBoxInit(comboBoxesToInit);
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo error popup if failure
        }
    }

    /**
     *
     * @param comboBoxLinkedList
     * @throws IOException when IO error getting teams from disk
     */
    private void teamComboBoxInit(LinkedList<ComboBox<Team>> comboBoxLinkedList) throws IOException {
        //get all teams from disk and add to observable list
        ObservableList<Team> teamObservableList = FXCollections.observableArrayList(); //todo simplify, no need to convert
        LinkedList<Team> teams = Team.getAllTeamsFromDisk();
        teamObservableList.addAll(teams);

        //for each combo box in input array
        for (ComboBox<Team> comboBox :
                comboBoxLinkedList) {
            comboBox.setItems(teamObservableList); //add team list to combobox
            teamComboBoxSetConverter(comboBox); //apply setConverter to combobox team elements
        }
    }

    private void teamComboBoxSetConverter(ComboBox<Team> aComboBox) {
        aComboBox.setConverter(new StringConverter<Team>() { //https://stackoverflow.com/a/38367739/10714709
            @Override
            public String toString(Team object) {
                return object.getTeamName();
            }

            @Override
            public Team fromString(String string) {
                return null;
            }
        });
    }
}
