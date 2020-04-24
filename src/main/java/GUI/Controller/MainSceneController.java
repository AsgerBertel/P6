package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    private TextField txtTopic, txtLocation, txtOpinion, txtDate;
    @FXML
    private Button btnUpdate;
    @FXML
    private TableView tableViewLeft;

    private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

    @FXML
    private VBox leftSide;
    ResultSet resultSet;

    public void initialize() {
        searchBarContextMenu.setStyle("-fx-pref-width: 200");
        comboLocation.setItems(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "none"));
        comboOpinion.setItems(FXCollections.observableArrayList("opinion", "none"));
        comboTopic.setItems(FXCollections.observableArrayList("subtopic", "toptopic", "none"));
        comboDate.setItems(FXCollections.observableArrayList("day", "month", "year", "none"));
        initializeTopicOnKeyPressedEvent(comboTopic);
        initializeOpinionOnKeyPressedEvent(comboOpinion);
        initializeLocationOnKeyPressedEvent(comboLocation);
        initializeateDateOnKeyPressedEvent(comboDate);
        tableViewLeft.setItems(data);
    }

    public void buildTable(ResultSet resultSet_Tableview) throws SQLException {
        data.clear();
        tableViewLeft.getColumns().clear();
        tableViewLeft.getItems().clear();
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

    public void setAddProductText(String menuItem, TextField currentSearchbar) {
        currentSearchbar.setText(menuItem);
    }

    public void loadView() {
        try {
            String strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
            String strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();
            String strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
            String strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
            String viewQuery = QueryManager.selectView(strComboTopic + strComboLocation + strComboDate + strComboOpinion);
            String viewQueryWhereFirst = QueryManager.selectView(strComboTopic + strComboLocation + strComboDate + strComboOpinion) + " WHERE ";
            String viewQueryWhereSecond = "";
            boolean isWhere = false;
            if (txtTopic.getText().trim().isEmpty() && txtDate.getText().trim().isEmpty() && txtLocation.getText().trim().isEmpty() && txtOpinion.getText().trim().isEmpty()) {
                viewQuery = QueryManager.selectView(strComboTopic + strComboLocation + strComboDate + strComboOpinion);
                isWhere = false;
            }
            if (!txtLocation.getText().trim().isEmpty() || !txtLocation.getText().isEmpty()) {
                viewQueryWhereSecond += strComboLocation + "=" + "'" + txtLocation.getText() + "'" + "";
                isWhere = true;
            }
            if (!txtTopic.getText().trim().isEmpty() || !txtTopic.getText().isEmpty()) {
                viewQueryWhereSecond += strComboTopic + "=" + "'" + txtTopic.getText() + "'" + "";
                isWhere = true;
            }
            if (!txtOpinion.getText().trim().isEmpty() || !txtOpinion.getText().isEmpty()) {
                viewQueryWhereSecond += strComboOpinion + "=" + "'" + txtOpinion.getText() + "'" + "";
                isWhere = true;
            }
            if (!txtDate.getText().trim().isEmpty() || !txtDate.getText().isEmpty()) {
                viewQueryWhereSecond += strComboDate = "=" + "'" + txtDate.getText() + "'" + "";
                isWhere = true;
            }
            viewQueryWhereFirst += viewQueryWhereSecond;
            if (isWhere)
                resultSet = ConnectionManager.selectSQL(viewQueryWhereFirst);
            else
                resultSet = ConnectionManager.selectSQL(viewQuery);
         buildTable(resultSet);

        } catch (NullPointerException e) {
            System.out.println("Choose some");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initializeOpinionOnKeyPressedEvent(ComboBox comboOpinion) {
        txtOpinion.setOnKeyReleased(event -> {
            boolean isOpinion;
            try {
                if (comboOpinion.getSelectionModel().getSelectedItem().toString().equals("opinion")) {
                    isOpinion = true;
                } else {
                    isOpinion = false;
                }
                searchBarContextMenu.getItems().clear();
                searchBarContextMenu.loadOpinions(txtOpinion.getText(), isOpinion, txtOpinion);
                searchBarContextMenu.show(txtOpinion, Side.BOTTOM, 0, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("nothing has been chosen");
            }
        });
    }

    private void initializeTopicOnKeyPressedEvent(ComboBox comboTopic) {

        //searchbar for topics
        txtTopic.setOnKeyReleased(event -> {
            boolean isTopTopic, isSubTopic;
            try {
                if (comboTopic.getSelectionModel().getSelectedItem().toString().equals("toptopic")) {
                    isSubTopic = false;
                    isTopTopic = true;
                } else if (comboTopic.getSelectionModel().getSelectedItem().toString().equals("subtopic")) {
                    isSubTopic = true;
                    isTopTopic = false;
                } else {
                    isSubTopic = false;
                    isTopTopic = false;
                }
                searchBarContextMenu.getItems().clear();
                searchBarContextMenu.loadTopics(txtTopic.getText(), isTopTopic, isSubTopic, txtTopic);
                searchBarContextMenu.show(txtTopic, Side.BOTTOM, 0, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("nothing has been chosen");
            }
        });
    }

    private void initializeLocationOnKeyPressedEvent(ComboBox comboLocation) {

        //searchbar for topics
        txtLocation.setOnKeyReleased(event -> {
            boolean isDistrict, isCounty, isCity, isCountry;
            try {
                if (comboLocation.getSelectionModel().getSelectedItem().toString().equals("district")) {
                    isDistrict = true;
                    isCounty = false;
                    isCity = false;
                    isCountry = false;
                } else if (comboLocation.getSelectionModel().getSelectedItem().toString().equals("county")) {
                    isDistrict = false;
                    isCounty = true;
                    isCity = false;
                    isCountry = false;

                } else if (comboLocation.getSelectionModel().getSelectedItem().toString().equals("city")) {
                    isDistrict = false;
                    isCounty = false;
                    isCity = true;
                    isCountry = false;

                } else if (comboLocation.getSelectionModel().getSelectedItem().toString().equals("country")) {
                    isDistrict = false;
                    isCounty = false;
                    isCity = false;
                    isCountry = true;

                } else {
                    isDistrict = false;
                    isCounty = false;
                    isCity = false;
                    isCountry = false;
                }
                searchBarContextMenu.getItems().clear();
                searchBarContextMenu.loadLocation(txtLocation.getText(), isDistrict, isCounty, isCity, isCountry, txtLocation);
                searchBarContextMenu.show(txtLocation, Side.BOTTOM, 0, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("nothing has been chosen");
            }
        });
    }

    private void initializeateDateOnKeyPressedEvent(ComboBox comboDate) {

        //searchbar for topics
        txtDate.setOnKeyReleased(event -> {
            boolean isDay, isMonth, isYear;
            try {
                if (comboDate.getSelectionModel().getSelectedItem().toString().equals("day")) {
                    isDay = true;
                    isMonth = false;
                    isYear = false;
                } else if (comboDate.getSelectionModel().getSelectedItem().toString().equals("month")) {
                    isDay = false;
                    isMonth = true;
                    isYear = false;

                } else if (comboDate.getSelectionModel().getSelectedItem().toString().equals("year")) {
                    isDay = false;
                    isMonth = false;
                    isYear = true;

                } else {
                    isDay = false;
                    isMonth = false;
                    isYear = false;
                }
                searchBarContextMenu.getItems().clear();
                searchBarContextMenu.loadDate(txtDate.getText(), isDay, isMonth, isYear, txtDate);
                searchBarContextMenu.show(txtDate, Side.BOTTOM, 0, 0);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("nothing has been chosen");
            }
        });
    }

}
