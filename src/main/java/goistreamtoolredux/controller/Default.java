package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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

    @FXML
    void selectTeamA(ActionEvent event) {
        Team selectedTeam = TeamAComboBox.getSelectionModel().getSelectedItem();
        System.out.println(selectedTeam.getTeamLogo());
    }

    @FXML
    void selectTeamB(ActionEvent event) {

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
