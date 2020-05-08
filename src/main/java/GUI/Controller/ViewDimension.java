package GUI.Controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ViewDimension {
    //todo better name for class
    private ViewDimensionEnum viewDimension;
    private ComboBox comboBox;
    private TextField drill;

    public ViewDimension(ViewDimensionEnum viewDimension, ComboBox comboBox, TextField drill) {
        this.viewDimension = viewDimension;
        this.comboBox = comboBox;
        this.drill = drill;
    }

    public ComboBox getComboBox(){
        return this.comboBox;
    }
    public ViewDimensionEnum getViewDimension() {
        return viewDimension;
    }

    public String getComboBoxText(){
        if(this.comboBox.getSelectionModel().getSelectedItem().toString().equals("all"))
            return "none";
        return this.comboBox.getSelectionModel().getSelectedItem().toString();
    }

    public String getDrillText() {
        return drill.getText().trim();
    }
}
