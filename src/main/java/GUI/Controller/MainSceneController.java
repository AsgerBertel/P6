package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.util.Callback;

import javax.xml.transform.Result;
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
    @FXML
    private TableView tableViewLeft;

    private ObservableList<ObservableList> data = FXCollections.observableArrayList();

    ResultSet resultSet_Tableview;
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
        try {
            resultSet_Tableview = ConnectionManager.selectSQL(QueryManager.selectAllFromSubTopics());
            buildTable(resultSet_Tableview);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void buildTable(ResultSet resultSet_Tableview) throws SQLException {
        for(int i = 0; i < resultSet_Tableview.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = new TableColumn<>(resultSet_Tableview.getMetaData().getColumnName(i+1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            tableViewLeft.getColumns().addAll(col);
            System.out.println(col.getText() + "["+i+"]" + "has been added");
        }
        while(resultSet_Tableview.next()){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i = 1; i <= resultSet_Tableview.getMetaData().getColumnCount();i++){
                row.add(resultSet_Tableview.getString(i));
            }
            data.add(row);
        }

        tableViewLeft.setItems(data);
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
