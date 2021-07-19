package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import goistreamtoolredux.algorithm.FileManager;
import goistreamtoolredux.algorithm.Team;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class TeamPane {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    public AnchorPane anchorPane;

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

    @FXML // fx:id="swapABButton"
    private JFXButton swapABButton; // Value injected by FXMLLoader

    @FXML // fx:id="swapCDButton"
    private JFXButton swapCDButton; // Value injected by FXMLLoader

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

    private static Team selectedATeam;
    private static Team selectedBTeam;
    private static Team selectedCTeam;
    private static Team selectedDTeam;


    public void save() {
        // save team selections
        setTeamIfNotNull(selectedATeam, "A");
        setTeamIfNotNull(selectedBTeam, "B");
        setTeamIfNotNull(selectedCTeam, "C");
        setTeamIfNotNull(selectedDTeam, "D");

        //snackBar popup, team infos saved
        JFXSnackbar bar = new JFXSnackbar(anchorPane);
        bar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Updating Teams...",null,null),new Duration(1000)));
    }

    /**
     * Helper for {@link #save()}
     */
    private void setTeamIfNotNull(Team aTeam, String letter) {
        if (aTeam != null) {
            try {
                FileManager.setTeam(aTeam.getTeamName(), letter);
            } catch (IOException exception) {
                exception.printStackTrace();
                //todo, handle
            }
        } else {
            //remove content
            try {
                FileManager.removeTeam(letter);
            } catch (IOException exception) {
                //todo, handle
                exception.printStackTrace();
            }
        }

    }

    @FXML
    void selectTeamA(ActionEvent event) {
        selectedATeam = teamAComboBox.getSelectionModel().getSelectedItem();

        if (selectedATeam == null) { //comboBox selection has been cleared
            //remove image and text from UI
            teamAImage.setImage(null);
            teamALabel.setText(null);

        } else {
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

    }

    @FXML
    void selectTeamB(ActionEvent event) {
        selectedBTeam = teamBComboBox.getSelectionModel().getSelectedItem();

        if (selectedBTeam == null) { //comboBox selection has been cleared
            //remove image and text from UI
            teamBImage.setImage(null);
            teamBLabel.setText(null);

        } else {
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
    }

    @FXML
    void selectTeamC(ActionEvent event) {
        selectedCTeam = teamCComboBox.getSelectionModel().getSelectedItem();

        if (selectedCTeam == null) { //comboBox selection has been cleared
            //remove image and text from UI
            teamCImage.setImage(null);
            teamCLabel.setText(null);

        } else {

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

    }

    @FXML
    void selectTeamD(ActionEvent event) {
        selectedDTeam = teamDComboBox.getSelectionModel().getSelectedItem();

        if (selectedDTeam == null) { //comboBox selection has been cleared
            //remove image and text from UI
            teamDImage.setImage(null);
            teamDLabel.setText(null);

        } else {

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
    }

    @FXML
    void swapAB(ActionEvent event) {
        if (!(teamBComboBox.getSelectionModel().isEmpty() || teamAComboBox.getSelectionModel().isEmpty())) { //neither are empty

            //Temp B
            Team tempB = selectedBTeam;

            //A -> B
            teamBComboBox.getSelectionModel().select(selectedATeam);

            //B -> A
            teamAComboBox.getSelectionModel().select(tempB);

            //Full Update
            selectTeamA(null);
            selectTeamB(null);
        }
    }

    @FXML
    void swapCD(ActionEvent event) {
        if (!(teamCComboBox.getSelectionModel().isEmpty() || teamDComboBox.getSelectionModel().isEmpty())) { //neither are empty

            //Temp D
            Team tempD = selectedDTeam;

            //C -> D
            teamDComboBox.getSelectionModel().select(selectedCTeam);

            //D -> C
            teamCComboBox.getSelectionModel().select(tempD);
        }
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
            LinkedList<Team> teams = Team.getAllTeamsFromDisk();
            teamComboBoxInit(comboBoxesToInit, teams);
            teamComboboxSelectionInit(teams);
        } catch (IOException exception) {
            exception.printStackTrace();
            //todo error popup if failure
        }


    }

    /**
     * Sets the current selection for each combobox based on team files in disk.
     *
     * Loops through each team in the teamList and sees if the team's name is equal to the name of
     * any of the teams set in disk. If so, selects it for the appropriate comboBox
     *
     * @param teamsList list of teams to search through for matching selects
     */
    private void teamComboboxSelectionInit(LinkedList<Team> teamsList) {
        //get current values from disk
        String currentAName = Team.getTeamFromDisk("A");
        String currentBName = Team.getTeamFromDisk("B");
        String currentCName = Team.getTeamFromDisk("C");
        String currentDName = Team.getTeamFromDisk("D");

        //for each team
        for (Team aTeam :
                teamsList) {
            //if team is right team
            String teamName = aTeam.getTeamName();
            if (teamName.equals(currentAName)) {
                teamAComboBox.getSelectionModel().select(aTeam);
                selectTeamA(null);
            } else if (teamName.equals(currentBName)) {
                teamBComboBox.getSelectionModel().select(aTeam);
                selectTeamB(null);
            } else if (teamName.equals(currentCName)) {
                teamCComboBox.getSelectionModel().select(aTeam);
                selectTeamC(null);
            } else if (teamName.equals(currentDName)) {
                teamDComboBox.getSelectionModel().select(aTeam);
                selectTeamD(null);
            }
        }
    }

    /**
     *
     * @param comboBoxLinkedList
     * @param teams
     * @throws IOException when IO error getting teams from disk
     */
    private void teamComboBoxInit(LinkedList<ComboBox<Team>> comboBoxLinkedList, LinkedList<Team> teams) {
        //get all teams from disk
        Collections.sort(teams);
        //add to observable list
        ObservableList<Team> teamObservableList = FXCollections.observableArrayList();
        teamObservableList.addAll(teams);

        //for each combo box in input array
        for (ComboBox<Team> comboBox :
                comboBoxLinkedList) {
            comboBox.setItems(teamObservableList); //add team list to combobox
            teamComboBoxSetConverter(comboBox); //apply setConverter to combobox team elements
        }
    }

    private void sortTeamList(LinkedList<Team> teamLinkedList) {
        for (int i = 0; i < teamLinkedList.size(); i++) {

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

    /**
     * Clears the currently selected data, and empties the associated files.
     */
    public void clear() {

        //clear UI
        teamAComboBox.getSelectionModel().clearSelection(); //change is heard by FXML!
        teamBComboBox.getSelectionModel().clearSelection();
        teamCComboBox.getSelectionModel().clearSelection();
        teamDComboBox.getSelectionModel().clearSelection();

        //clear files
        save();
    }
}
