package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TeamPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="leftVBox"
    private VBox leftVBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamAComboBox"
    private JFXComboBox<Team> teamAComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamALabel"
    private Label teamALabel; // Value injected by FXMLLoader

    @FXML // fx:id="teamAImage"
    private ImageView teamAImage; // Value injected by FXMLLoader

    @FXML // fx:id="teamCComboBox"
    private JFXComboBox<Team> teamCComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamCLabel"
    private Label teamCLabel; // Value injected by FXMLLoader

    @FXML // fx:id="teamCImage"
    private ImageView teamCImage; // Value injected by FXMLLoader

    @FXML // fx:id="rightVBox"
    private VBox rightVBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamBComboBox"
    private JFXComboBox<Team> teamBComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamBLabel"
    private Label teamBLabel; // Value injected by FXMLLoader

    @FXML // fx:id="teamBImage"
    private ImageView teamBImage; // Value injected by FXMLLoader

    @FXML // fx:id="teamDComboBox"
    private JFXComboBox<Team> teamDComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="teamDLabel"
    private Label teamDLabel; // Value injected by FXMLLoader

    @FXML // fx:id="teamDImage"
    private ImageView teamDImage; // Value injected by FXMLLoader

    private Team selectedATeam;
    private Team selectedBTeam;
    private Team selectedCTeam;
    private Team selectedDTeam;

    @FXML
    void selectTeamA(ActionEvent event) {
        selectedATeam = teamAComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedATeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamAImage.setImage(null);
        } else {
            //update image
            teamAImage.setImage(new Image(selectedATeam.getTeamLogo().toUri().toString()));
        }

        //update label
        teamALabel.setText(selectedATeam.getAbbreviatedName());
    }

    @FXML
    void selectTeamB(ActionEvent event) {
        selectedBTeam = teamBComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedBTeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamBImage.setImage(null);
        } else {
            //update image
            teamBImage.setImage(new Image(selectedBTeam.getTeamLogo().toUri().toString()));
        }

        //update label
        teamBLabel.setText(selectedBTeam.getAbbreviatedName());
    }

    @FXML
    void selectTeamC(ActionEvent event) {
        selectedCTeam = teamCComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedCTeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamCImage.setImage(null);
        } else {
            //update image
            teamCImage.setImage(new Image(selectedCTeam.getTeamLogo().toUri().toString()));
        }

        //update label
        teamCLabel.setText(selectedCTeam.getAbbreviatedName());
    }

    @FXML
    void selectTeamD(ActionEvent event) {
        selectedDTeam = teamDComboBox.getSelectionModel().getSelectedItem();
        //update UI image
        if (selectedDTeam.getTeamLogo() == null) { //if there isn't an image
            //delete image
            teamDImage.setImage(null);
        } else {
            //update image
            teamDImage.setImage(new Image(selectedDTeam.getTeamLogo().toUri().toString()));
        }

        //update label
        teamDLabel.setText(selectedDTeam.getAbbreviatedName());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert leftVBox != null : "fx:id=\"leftVBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamAComboBox != null : "fx:id=\"teamAComboBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamALabel != null : "fx:id=\"teamALabel\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamAImage != null : "fx:id=\"teamAImage\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamCComboBox != null : "fx:id=\"teamCComboBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamCLabel != null : "fx:id=\"teamCLabel\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamCImage != null : "fx:id=\"teamCImage\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamBComboBox != null : "fx:id=\"teamBComboBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamBLabel != null : "fx:id=\"teamBLabel\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamBImage != null : "fx:id=\"teamBImage\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamDComboBox != null : "fx:id=\"teamDComboBox\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamDLabel != null : "fx:id=\"teamDLabel\" was not injected: check your FXML file 'TeamPane.fxml'.";
        assert teamDImage != null : "fx:id=\"teamDImage\" was not injected: check your FXML file 'TeamPane.fxml'.";

        //init team combo boxes
        LinkedList<ComboBox<Team>> comboBoxesToInit = new LinkedList<>();
        comboBoxesToInit.add(teamAComboBox);
        comboBoxesToInit.add(teamBComboBox);
        comboBoxesToInit.add(teamCComboBox);
        comboBoxesToInit.add(teamDComboBox);
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
