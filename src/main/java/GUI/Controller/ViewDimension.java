package GUI.Controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ViewDimension {
    //todo better name for class
    private ViewDimensionEnum viewDimension;
    private ComboBox comboBox;
    private TextField drill, where;

    public ViewDimension(ViewDimensionEnum viewDimension, ComboBox comboBox, TextField drill) {
        this.viewDimension = viewDimension;
        this.comboBox = comboBox;
        this.drill = drill;
        this.where = where;
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

    public String getWhereText() {
        return where.getText().trim();
    }
}
