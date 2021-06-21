package goistreamtoolredux.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

class AutoCompleteComboBoxListener<T>{

    private JFXComboBox<T> comboBox;
    private ObservableList<T> data;

    AutoCompleteComboBoxListener(final JFXComboBox<T> comboBox) {
        this.comboBox = comboBox;
        data = comboBox.getItems();

        //make ComboBox editable
        this.comboBox.setEditable(true);

        //Have ComboBox call handleOnKeyReleased method when key is pressed
        this.comboBox.setOnKeyReleased(this::handleOnKeyReleased);
    }

    /**
     * Autocomplete ComboBox(drop-down lists) when typing into text field
     * @param event
     */
    public void handleOnKeyReleased(KeyEvent event) {

        //Get whatever key was pressed by user
        KeyCode keyCode = event.getCode();

        //If up or down arrow is pressed, move caret to end of long name
        if(keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
            comboBox.getEditor().positionCaret(comboBox.getEditor().getText().length());
            return;
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }

        //Create list to hold filtered items
        ObservableList<T> list = FXCollections.observableArrayList();
        //loop through all of the data in ComboBox
        for (T aData : data) {
            //If string in drop down list matches the key being typed, add it to the filtered list
            if (String.valueOf(aData).toLowerCase().startsWith(this.comboBox
                    .getEditor().getText().toLowerCase())) {
                list.add(aData);
            }
        }

        //Reposition caret to be where you are typing
        String t = comboBox.getEditor().getText();
        comboBox.getEditor().positionCaret(t.length());

        //Set items in drop down list to be the filtered items
        comboBox.setItems(list);

        //Show the drop down list for the filtered items
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

}