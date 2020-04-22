package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainSceneController {
    SearchBarContextMenu searchBarContextMenu = new SearchBarContextMenu(this);
    @FXML
    private ComboBox comboLocation, comboOpinion, comboTopic, comboDate;
    @FXML
    private TextField txtTopic;
    @FXML
    private Button btnUpdate;
    ResultSet resultSet;

    public void initialize() {
        searchBarContextMenu.setStyle("-fx-pref-width: 200");
        comboLocation.setItems(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "none"));
        comboOpinion.setItems(FXCollections.observableArrayList("opinion", "none"));
        comboTopic.setItems(FXCollections.observableArrayList("subtopic", "toptopic", "none"));
        comboDate.setItems(FXCollections.observableArrayList("day", "month", "year", "none"));
        txtTopic.setOnKeyPressed(event -> {
            try {
                searchBarContextMenu.getItems().clear();
                searchBarContextMenu.loadProducts(txtTopic.getText());
                searchBarContextMenu.show(txtTopic, Side.BOTTOM, 0, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public void UpdateViews() {
        ViewGenerator viewGenerator = new ViewGenerator();
        viewGenerator.runUpdate();

    }

    public void setAddProductText(String menuItem) {
        txtTopic.setText(menuItem);
    }

    public void loadView() {
        try {
            String strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
            String strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();
            String strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
            String strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
            if (strComboTopic.equals("toptopic")) {
                resultSet = ConnectionManager.selectSQL(QueryManager.selectTopicFromView(txtTopic.getText(), strComboTopic + strComboLocation + strComboDate + strComboOpinion));
           while (resultSet.next())
               System.out.println(resultSet.getString(1));
            }
        } catch (NullPointerException e) {
            System.out.println("Choose some");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

       
    }
}
