package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import GUI.Other.PopularityManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithmType;
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
import org.apache.spark.sql.execution.columnar.NULL;

import javax.xml.transform.Result;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainSceneController {
    SearchBarContextMenu searchBarContextMenu = new SearchBarContextMenu(this);
    private ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
    PopularityManager popularityManager = new PopularityManager();
    private boolean isViewGenInitialised = false;
    private ViewGenerator viewGenerator = new ViewGenerator();
    private HashMap<String, String> viewNameSumOrCountMap = new HashMap<>();
    private boolean isUpdated = true;

    @FXML
    private ComboBox comboLocation, comboOpinion, comboTopic, comboDate;
    @FXML
    private TextField txtTopic, txtLocation, txtOpinion, txtDate, txtDrillDate, txtDrillOpinion, txtDrillLocation, txtDrillTopic;
    @FXML
    private Button btnUpdate, btnSearch;
    @FXML
    private TableView tableViewLeft;

    @FXML
    private VBox leftSide;
    ResultSet resultSet;
    ArrayList<String> locationDimension = new ArrayList<>();
    ArrayList<String> dateDimension = new ArrayList<>();
    ArrayList<String> topicDimension = new ArrayList<>();
    ArrayList<String> opinionDimension = new ArrayList<>();

    String strComboTopic;
    String strComboOpinion;
    String strComboLocation;
    String strComboDate;
    int index;
    int indexLocation = -1;
    int indexDate = -1;
    int indexOpinion = -1;
    int indexTopic = -1;

    String viewName = "";
    String selectQuery = "SELECT * FROM ";
    String innerJoinQueryLocationFirst = "";
    String innerJoinQueryLocationSecond = "";
    String innerJoinQueryDateFirst = "";
    String innerJoinQueryDateSecond = "";
    String innerJoinQueryOpinionFirst = "";
    String innerJoinQueryOpinionSecond = "";
    String innerJoinQueryTopicFirst = "";
    String innerJoinQueryTopicSecond = "";
    String whereQuery = " WHERE ";

    public void initialize() {
        locationDimension.addAll(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country"));
        dateDimension.addAll(FXCollections.observableArrayList("day", "month", "year"));
        topicDimension.addAll(FXCollections.observableArrayList("subtopic", "toptopic"));
        opinionDimension.addAll(FXCollections.observableArrayList("opinion"));

        searchBarContextMenu.setStyle("-fx-pref-width: 200");
        comboLocation.setItems(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "all"));
        comboOpinion.setItems(FXCollections.observableArrayList("opinion", "all"));
        comboTopic.setItems(FXCollections.observableArrayList("subtopic", "toptopic", "all"));
        comboDate.setItems(FXCollections.observableArrayList("day", "month", "year", "all"));
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
        for (int i = 0; i < resultSet_Tableview.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn col = new TableColumn<>(resultSet_Tableview.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            tableViewLeft.getColumns().addAll(col);

            System.out.println(col.getText() + "[" + i + "]" + "has been added");
        }
        while (resultSet_Tableview.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet_Tableview.getMetaData().getColumnCount(); i++) {
                row.add(resultSet_Tableview.getString(i));
            }
            data.add(row);
        }

        tableViewLeft.setItems(data);
    }

    public void updateViews() {
        btnUpdate.setDisable(false);
        if (!this.isViewGenInitialised) {
            viewGenerator = new ViewGenerator();
            viewGenerator.init(GreedyAlgorithmType.POPULARITY);
        }
        try {
            viewGenerator.updateViews();
            isUpdated = true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            btnUpdate.setDisable(false);
        }
    }

    public void setAddProductText(String menuItem, TextField currentSearchbar) {
        currentSearchbar.setText(menuItem);
    }

    private HashMap<String, String> extractViewNameAndAggregate(ResultSet rs) throws SQLException {
        HashMap<String, String> newViewNameSumOrCountMap = new HashMap<>();
        while (rs.next()) {
            if (rs.getString(2).contains("AS sum")) {
                newViewNameSumOrCountMap.put(rs.getString(1), "sum");
            } else {
                newViewNameSumOrCountMap.put(rs.getString(1), "count");
            }
        }
        return newViewNameSumOrCountMap;
    }

    private HashMap<String, String> getViewNameSumOrCountMap() {
        HashMap<String, String> newViewNameSumOrCountMap = new HashMap<>();
        if (isUpdated) {
            try {
                newViewNameSumOrCountMap.putAll(extractViewNameAndAggregate(ConnectionManager.selectSQL(QueryManager.selectAllMaterializedlViewNamesAndDefinitions)));
                newViewNameSumOrCountMap.putAll(extractViewNameAndAggregate(ConnectionManager.selectSQL(QueryManager.selectAllVirtualViewNamesAndDefinitions)));
                isUpdated = false;
                viewNameSumOrCountMap = newViewNameSumOrCountMap;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return viewNameSumOrCountMap;
    }

    private String getComboString(ComboBox box) {
        if (box.getSelectionModel().getSelectedItem().toString().equals("all")) {
            return "none";
        }
        return box.getSelectionModel().getSelectedItem().toString();
    }

    private String addMeasures() {
        return null;
    }

    private String selectQuery(String topic, String location, String date, String opinion) {
        String viewName = topic + location + date + opinion;
        String[] array = {topic, location, date, opinion};
        StringBuilder sb = new StringBuilder();
        //append select
        sb.append("SELECT ");
        for (String s : array) {
            if (s.equals("coordinate")) {
                sb.append("lat, long,");
            } else {
                sb.append(s).append(",");
            }
        }
        sb.append(getViewNameSumOrCountMap().get(viewName));
        //ADD MEASURES
        //sb.append(addMeasures());
        sb.append(" FROM ").append(viewName);
        return sb.toString();
    }

    public void loadView() {
        btnSearch.setDisable(false);
        try {
            String strComboTopic = getComboString(this.comboTopic);
            String strComboDate = getComboString(this.comboDate);
            String strComboLocation = getComboString(this.comboLocation);
            String strComboOpinion = getComboString(this.comboOpinion);

            String viewQuery = selectQuery(strComboTopic, strComboLocation, strComboDate, strComboOpinion);
            String viewQueryWhereFirst = selectQuery(strComboTopic, strComboLocation, strComboDate, strComboOpinion) + " WHERE ";
            String viewQueryWhereSecond = "";

            popularityManager.updatePopularityValue((strComboTopic + strComboLocation + strComboDate + strComboOpinion).trim());
            boolean isWhere = false;
            if (txtTopic.getText().trim().isEmpty() && txtDate.getText().trim().isEmpty() && txtLocation.getText().trim().isEmpty() && txtOpinion.getText().trim().isEmpty()) {
                viewQuery = selectQuery(strComboTopic, strComboLocation, strComboDate, strComboOpinion);
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
                viewQueryWhereSecond += strComboDate + " = " + " " + txtDate.getText() + "";
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
        } finally {
            btnSearch.setDisable(false);
        }
    }

    public void drillDown() {
        strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
        strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
        strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
        strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();

        strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
        if (strComboTopic.equals("all"))
            strComboTopic = "none";

        strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();
        if (strComboDate.equals("all"))
            strComboDate = "none";

        strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
        if (strComboLocation.equals("all"))
            strComboLocation = "none";

        strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
        if (strComboOpinion.equals("all"))
            strComboOpinion = "none";


        boolean isWhere = false;


        if (!txtDrillTopic.getText().isEmpty()) {
            whereQuery = modifyDrillQuery(isWhere, whereQuery, topicDimension, txtDrillTopic.getText(), strComboTopic, comboTopic,indexTopic);


        } else {
            viewName += strComboTopic;
        }

        if (!txtDrillLocation.getText().isEmpty()) {
            whereQuery = modifyDrillQuery(isWhere, whereQuery, locationDimension, txtDrillLocation.getText(), strComboLocation, comboLocation,indexLocation);

        } else {
            viewName += strComboLocation;
        }

        if (!txtDrillDate.getText().isEmpty()) {
            whereQuery = modifyDrillQuery(isWhere, whereQuery, dateDimension, txtDrillDate.getText(), strComboDate, comboDate,indexDate);

        } else {
            viewName += strComboDate;
        }

        if (!txtDrillOpinion.getText().isEmpty()) {
            whereQuery = modifyDrillQuery(isWhere, whereQuery, opinionDimension, txtDrillOpinion.getText(), strComboOpinion, comboOpinion,indexOpinion);

        } else {
            viewName += strComboOpinion;
        }
        String locationInnerJoin = "";
        String dateInnerJoin = "";
        String opinionInnerJoin = "";
        String topicInnerJoin = "";

        if (!innerJoinQueryLocationFirst.isEmpty())
            locationInnerJoin = innerJoinQueryLocationFirst + viewName + innerJoinQueryLocationSecond;
        if (!innerJoinQueryDateFirst.isEmpty())
            dateInnerJoin = innerJoinQueryDateFirst + viewName + innerJoinQueryDateSecond;
        if (!innerJoinQueryOpinionFirst.isEmpty())
            opinionInnerJoin = innerJoinQueryOpinionFirst + viewName + innerJoinQueryOpinionSecond;
        if (!innerJoinQueryTopicFirst.isEmpty())
            topicInnerJoin = innerJoinQueryTopicFirst + viewName + innerJoinQueryTopicSecond;
        System.out.println(selectQuery + viewName + locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery);
        ResultSet resultSet = null;
        try {
            resultSet = ConnectionManager.selectSQL(selectQuery + viewName + locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery);
            buildTable(resultSet);
            txtDrillOpinion.clear();
            txtDrillTopic.clear();
            txtDrillDate.clear();
            txtDrillLocation.clear();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        resetGlobalVariables();

    }

    public void resetGlobalVariables() {
        viewName = "";
         selectQuery = "SELECT * FROM ";
         innerJoinQueryLocationFirst = "";
         innerJoinQueryLocationSecond = "";
         innerJoinQueryDateFirst = "";
         innerJoinQueryDateSecond = "";
         innerJoinQueryOpinionFirst = "";
         innerJoinQueryOpinionSecond = "";
         innerJoinQueryTopicFirst = "";
         innerJoinQueryTopicSecond = "";
         whereQuery = " WHERE ";
    }

    private String modifyDrillQuery(boolean isWhere, String whereQuery, ArrayList<String> dimension, String drillTextFieldText, String strCombo, ComboBox combo, int IndexForDimension) {
        index = dimension.indexOf(strCombo);
        if (IndexForDimension == -1)
            IndexForDimension = index;
        else
            IndexForDimension -= 1;

        index -= 1;
        combo.setValue(dimension.get(index));
        if (index >= 0) {
            viewName += dimension.get(index);
            innerJoinQueryDateFirst = " INNER JOIN cube." + dimension.get(IndexForDimension) + " ON " + dimension.get(IndexForDimension) + "." + dimension.get(IndexForDimension) + "id = ";
            innerJoinQueryDateSecond = "." + dimension.get(IndexForDimension) + "id";
            if (isWhere)
                whereQuery += " AND " + dimension.get(IndexForDimension) + "." + dimension.get(IndexForDimension) + " =" + "'" + drillTextFieldText + "'";
            else
                whereQuery += dimension.get(IndexForDimension) + "." + dimension.get(IndexForDimension) + " =" + "'" + drillTextFieldText + "'";
            strCombo = dimension.get(index);
        }

        return whereQuery;
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
            if (!comboLocation.getSelectionModel().getSelectedItem().toString().equals("coordinate")) {
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
            } else {
                txtLocation.clear();
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
