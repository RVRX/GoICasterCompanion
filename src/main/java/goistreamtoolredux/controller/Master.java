/**
 * Sample Skeleton for 'Master.fxml' Controller Class
 */

package goistreamtoolredux.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Master {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="navigationSidebarVBox"
    private VBox navigationSidebarVBox; // Value injected by FXMLLoader

    @FXML // fx:id="rightVBox"
    private VBox rightVBox; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert rightVBox != null : "fx:id=\"rightVBox\" was not injected: check your FXML file 'Master.fxml'.";

        Node sideBar = null;

        try {
            rightVBox.getChildren().add(FXMLLoader.load(getClass().getResource("/goistreamtoolredux/fxml/Default.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
