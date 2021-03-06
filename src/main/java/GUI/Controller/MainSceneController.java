package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import GUI.Other.PopularityManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithmType;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;
import ViewCalculations.View;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.execution.columnar.NULL;
import org.postgresql.util.PSQLException;
import scala.util.matching.Regex;

import java.math.BigInteger;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainSceneController {
    @FXML
    private CheckBox chkFD, chkOD, chkIPD;
    SearchBarContextMenu searchBarContextMenu = new SearchBarContextMenu(this);
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();
    PopularityManager popularityManager = new PopularityManager();
    private boolean isViewGenInitialised = false;
    private ViewGenerator viewGenerator = new ViewGenerator();
    private HashMap<String, String> viewNameSumOrCountMap = new HashMap<>();
    private boolean isUpdated = true;
    private ArrayList<ViewDimension> viewDimensions = new ArrayList<>();
    private HashMap<ViewDimensionEnum, String> previousDrillJoins = new HashMap<>(), previousDrillWheres = new HashMap<>();
    private HashMap<ViewDimensionEnum, String> currentDrillJoins = new HashMap<>(), currentDrillWheres = new HashMap<>();
    String lastQuery;
    private boolean isDrillDown = false, isLastQueryDrillDown = false;
    @FXML
    private ComboBox comboLocation, comboOpinion, comboTopic, comboDate;
    @FXML
    private TextField txtDrillDate, txtDrillOpinion, txtDrillLocation, txtDrillTopic, txtTopRows, txtSliceValue, txtSliceCollum;
    @FXML
    private Button btnUpdate, btnSearch;
    @FXML
    private TableView tableViewLeft;
    @FXML
    private Label lblViewSize;

    @FXML
    private VBox leftSide;
    ResultSet resultSet;

    HashMap<ViewDimensionEnum, List<String>> dimensions = new HashMap<>();
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
    String select = "SELECT ";
    String from = " FROM ";
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
        dimensions.put(ViewDimensionEnum.LOCATION, Arrays.asList("coordinate", "district", "county", "city", "country", "all"));
        dimensions.put(ViewDimensionEnum.OPINION, Arrays.asList("opinion", "all"));
        dimensions.put(ViewDimensionEnum.TOPIC, Arrays.asList("toptopic", "all"));
        dimensions.put(ViewDimensionEnum.DATE, Arrays.asList("day", "month", "year", "all"));

        locationDimension.addAll(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "all"));
        dateDimension.addAll(FXCollections.observableArrayList("day", "month", "year", "all"));
        topicDimension.addAll(FXCollections.observableArrayList("toptopic", "all"));
        opinionDimension.addAll(FXCollections.observableArrayList("opinion", "all"));

        searchBarContextMenu.setStyle("-fx-pref-width: 200");
        comboLocation.setItems(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "all"));
        comboOpinion.setItems(FXCollections.observableArrayList("opinion", "all"));
        comboTopic.setItems(FXCollections.observableArrayList("toptopic", "all"));
        comboDate.setItems(FXCollections.observableArrayList("day", "month", "year", "all"));

      /*  initializeTopicOnKeyPressedEvent(comboTopic);
        initializeOpinionOnKeyPressedEvent(comboOpinion);
        initializeLocationOnKeyPressedEvent(comboLocation);
        initializeateDateOnKeyPressedEvent(comboDate);
        initializeOnlyNumericTextfield(txtTopRows);*/

        tableViewLeft.setItems(data);

        //ViewDimensions ORDER IS VERY IMPORTANT
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.TOPIC, comboTopic, txtDrillTopic));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.LOCATION, comboLocation, txtDrillLocation));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.DATE, comboDate, txtDrillDate));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.OPINION, comboOpinion, txtDrillOpinion));
    }

    public void clearTextfields() {
        txtDrillDate.clear();
        txtDrillOpinion.clear();
        txtDrillLocation.clear();
        txtDrillTopic.clear();
        txtSliceValue.clear();
        txtSliceCollum.clear();
    }


    public void initializeOnlyNumericTextfield(TextField txtTopRows) {
        txtTopRows.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTopRows.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    public ObservableList getColumnsFromResultset() {
        return tableViewLeft.getColumns();
    }
    public void buildTable(ResultSet resultSet_Tableview) throws SQLException {
        data.clear();
        tableViewLeft.getColumns().clear();
        tableViewLeft.getItems().clear();
        DecimalFormat df = new DecimalFormat("#.#####");
        ResultSetMetaData resultSetMetaData = resultSet_Tableview.getMetaData();
        for (int i = 0; i < resultSet_Tableview.getMetaData().getColumnCount(); i++) {
            System.out.println("amount of collums"+resultSet_Tableview.getMetaData().getColumnCount());
            final int j = i;

            System.out.println(j);
            TableColumn col = new TableColumn<>(resultSet_Tableview.getMetaData().getColumnName(i + 1));
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, Object>, ObservableValue<Object>>) param -> {
                return new SimpleObjectProperty<>(param.getValue().get(j)) {
                };
            });
            col.setComparator(makeComparator(resultSetMetaData.getColumnType(i+1)));

            tableViewLeft.getColumns().addAll(col);

            System.out.println(col.getText() + "[" + i + "]" + "has been added");
        }
        while (resultSet_Tableview.next()) {
            ObservableList row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet_Tableview.getMetaData().getColumnCount(); i++) {

                switch (resultSetMetaData.getColumnType(i)) {
                    case 12:

                        row.add(resultSet_Tableview.getString(i));
                        break;
                    case 4:

                        row.add(resultSet_Tableview.getInt(i));
                        break;
                    case 2:

                        row.add(df.format(resultSet_Tableview.getDouble(i)));
                        break;
                    case -5:
                        row.add(resultSet_Tableview.getLong(i));
                        break;
                    default:
                        System.out.println("YIKES");
                        break;
                }
            }
            data.add(row);
        }
        lblViewSize.setText("View size: " + data.size());
        tableViewLeft.setItems(data);
    }
    public Comparator makeComparator(int type){

        switch (type) {
            case 12:
                return new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                };
            case 4:
                return new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        int o1_numeric, o2_numeric;
                        o1_numeric = Integer.parseInt(o1.toString());
                        o2_numeric = Integer.parseInt(o2.toString());
                        if (o1_numeric < o2_numeric) return 1;
                        if (o1_numeric > o2_numeric) return -1;
                        return 0;
                    }
                };
            case 2:
                return new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        double o1_numeric, o2_numeric;
                        o1_numeric = Double.parseDouble(o1.toString().replace(",", "."));
                        o2_numeric = Double.parseDouble(o2.toString().replace(",", "."));
                        if (o1_numeric < o2_numeric) return 1;
                        if (o1_numeric > o2_numeric) return -1;
                        return 0;
                    }
                };
        }
        return null;
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

    private HashMap<String, String> extractViewNameAndAggregateMap(ResultSet rs) throws SQLException {
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
                newViewNameSumOrCountMap.putAll(extractViewNameAndAggregateMap(ConnectionManager.selectSQL(QueryManager.selectAllMaterializedlViewNamesAndDefinitions)));
                newViewNameSumOrCountMap.putAll(extractViewNameAndAggregateMap(ConnectionManager.selectSQL(QueryManager.selectAllVirtualViewNamesAndDefinitions)));
                isUpdated = false;
                viewNameSumOrCountMap = newViewNameSumOrCountMap;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return viewNameSumOrCountMap;
    }

    private String addSelectFrequencyDeviation() {
        StringBuilder sb = new StringBuilder();
        //sum or count
        String aggregate = getViewNameSumOrCountMap().get(getCurrView());
        sb.append(",avgval, (").append(aggregate).append("/avgval)*100 freqdev");
        return sb.toString();
    }

    private String addSelectOpinionDeviation() {
        return ", percentofmentions";
    }

    private String addMeasuresToSelect() {
        StringBuilder sb = new StringBuilder();
        //append to selects
        if (chkFD.isSelected()) {
            sb.append(addSelectFrequencyDeviation());
        }
        if (chkOD.isSelected()) {
            sb.append(addSelectOpinionDeviation());
        }
        if (sb.length() != 0)
            return sb.toString();
        return "";
    }

    private String addFrequencyDeviationInnerJoin() {
        //only requires topic to not be all.
        StringBuilder sb = new StringBuilder();
        //add inner join
        sb.append("inner join ");
        //select stmnt where we get the avg sum for given topics/opinions.
        sb.append("(SELECT ");
        for (ViewDimension vd : this.viewDimensions) {
            //add all dimensions EXCEPT for location (to get average of all locations)
            if (vd.getViewDimension().equals(ViewDimensionEnum.LOCATION) || vd.getComboBoxText().equals("none"))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //"avg(sum or count) avgval"
        sb.append("avg(").append(getViewNameSumOrCountMap().get(getCurrView())).append(") avgval FROM ").append("public.").append(getCurrView());
        //add group by
        sb.append(" GROUP BY ");
        for (ViewDimension vd : this.viewDimensions) {
            //add all dimensions EXCEPT for location (to get average of all locations)
            if (vd.getViewDimension().equals(ViewDimensionEnum.LOCATION) || vd.getComboBoxText().equals("none"))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length() - 2);
        //
        //add inner join conditions
        sb.append(") freqaverage on ");
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getViewDimension().equals(ViewDimensionEnum.LOCATION) || vd.getComboBoxText().equals("none"))
                continue;
            sb.append("view.").append(vd.getComboBoxText()).append(" = ").append("freqaverage.").append(vd.getComboBoxText()).append(" AND ");
        }
        //remove last AND
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String addDeviationInnerJoins() {
        StringBuilder sb = new StringBuilder();
        if (chkFD.isSelected())
            sb.append(addFrequencyDeviationInnerJoin());
        if (chkOD.isSelected())
            //IF OPINION IS ALL THEN WE CAN NOT INCLUDE THIS
            sb.append(addOpinionDeviationInnerJoin());
        //Only edge case is if opinion is set to all, then we should not include
        return sb.toString();
    }

    private String addOpinionDeviationInnerJoin() {
        return addOpinionDeviationPercentOfMentionsJoin();
    }

    private String addOpinionDeviationPercentOfMentionsJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append(" inner join (SELECT ");
        //add selects
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none"))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //add aggregate and viewname
        sb.append("(").append(getViewNameSumOrCountMap().get(getCurrView())).
                append("/totalsum)*100 percentofmentions FROM public.").append(getCurrView());
        //add totalsum inner join
        sb.append(" a inner join ").append(addTotalSumInnerJoin());
        //add group by and inner join conditions
        sb.append(" GROUP BY ");
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none"))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //add sum/count + total sum
        String aggregate = getViewNameSumOrCountMap().get(getCurrView());
        sb.append(aggregate).append(", totalsum) percentsum on ");
        //add inner join conditions
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none"))
                continue;
            sb.append(" percentsum.").append(vd.getComboBoxText()).
                    append(" = ").append("view.").append(vd.getComboBoxText()).
                    append(" AND ");
        }
        //remove last AND
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String addOpinionDeviationAveragePercentOfMentionsJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append(" inner join (SELECT ");
        //add selects
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //add aggregate and viewname
        sb.append("avg((").append(getViewNameSumOrCountMap().get(getCurrView())).
                append("/totalsum)*100) averagepercent FROM public.").append(getCurrView());
        //add totalsum inner join
        sb.append(" a inner join ").append(addTotalSumInnerJoin());
        //add group by and inner join conditions
        sb.append(" GROUP BY ");
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length() - 2);
        //add avgpercent and inner join conditions
        sb.append(") avgpercent on ");
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
                continue;
            sb.append(" avgpercent.").append(vd.getComboBoxText()).
                    append(" = ").append("view.").append(vd.getComboBoxText()).
                    append(" AND ");
        }
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String addTotalSumInnerJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append("( SELECT ");
        //Select
        for (ViewDimension vd : this.viewDimensions) {
            //select everything except for opinion
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //append sum aggregate of sum or count
        sb.append("sum(").append(getViewNameSumOrCountMap().get(getCurrView())).append(") totalsum");
        sb.append(" from public.").append(getCurrView());
        //group by
        sb.append(" group by ");
        for (ViewDimension vd : this.viewDimensions) {
            //select everything except for opinion
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length() - 2);
        sb.append(") b on ");
        //add inner join conditions
        for (ViewDimension vd : this.viewDimensions) {
            if (vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(" = b.").append(vd.getComboBoxText()).append(" AND ");
        }
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String getCurrView() {
        StringBuilder sb = new StringBuilder();
        for (ViewDimension vd : viewDimensions) {
            sb.append(vd.getComboBoxText());
        }
        return sb.toString();
    }

    private String selectQuery() {
        String viewName = getCurrView();
        StringBuilder sb = new StringBuilder();
        //append select
        sb.append("SELECT ");
        for (ViewDimension vd : viewDimensions) {
            if (vd.getComboBoxText().equals("coordinate")) {
                sb.append("view.lat, view.long,");
            } else {
                if (!vd.getComboBoxText().equals("none"))
                    sb.append("view.").append(vd.getComboBoxText()).append(",");
            }
        }
        sb.append("view.").append(getViewNameSumOrCountMap().get(viewName));
        //IF ANY MEASURES SELECTED, ADD MEASURES
        if (hasMeasures())
            sb.append(addMeasuresToSelect());
        sb.append(" FROM ").append(viewName).append(" view \n");
        return sb.toString();
    }

    private boolean hasMeasures() {
        return chkOD.isSelected() || chkFD.isSelected() || chkIPD.isSelected();
    }

    public void loadView() {
        btnSearch.setDisable(false);
        try {
            String query = selectQuery();
            //ADD INNER JOINS HERE
            query+=addDeviationInnerJoins();
            if(isDrillDown){
                query += drillInnerJoin();
                if(isLastQueryDrillDown){
                    query += addPreviousDrillDownJoins();
                }
                query += drillWhere();
                if(isLastQueryDrillDown){
                    query += addPreviousDrillDownWheres();
                }
                isDrillDown=false;
                isLastQueryDrillDown = true;
                previousDrillJoins = currentDrillJoins;
                previousDrillWheres = currentDrillWheres;
                currentDrillJoins = new HashMap<>();
                currentDrillWheres = new HashMap<>();
            }else{
                //this query is not a drill down, so we set flag to false and clear map
                isLastQueryDrillDown = false;
                previousDrillJoins = new HashMap<>();
            }
            lastQuery = query;
            if (txtTopRows.getText().isEmpty()) {
                System.out.println(ConnectionManager.selectSQL(query));
                buildTable(ConnectionManager.selectSQL(query));
            } else {
                buildTable(ConnectionManager.selectSQL(orderByQuery(query)));
            }
            popularityManager.updatePopularityValue(getCurrView());
        } catch (NullPointerException e) {
            System.out.println("Choose some");

        } catch (PSQLException e) {
            System.out.println("This view does not exist");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            clearTextfields();
            btnSearch.setDisable(false);
        }
    }

    private String addPreviousDrillDownWheres() {
        StringBuilder sb = new StringBuilder();
        for(String s : previousDrillWheres.values()){
            sb.append(" AND ").append(s);
        }
        return sb.toString();
    }

    private String addPreviousDrillDownJoins() {
        StringBuilder sb = new StringBuilder();
        for(String s : previousDrillJoins.values()){
            sb.append(s);
        }
        return sb.toString();
    }

    public String orderByQuery(String query) {
        if (!txtTopRows.getText().isEmpty())
            query = query + " ORDER BY " + getViewNameSumOrCountMap().get(getCurrView()) + " LIMIT " + txtTopRows.getText();
        return query;
    }

    private String drillWhere(){
        StringBuilder sb = new StringBuilder();
        ArrayList<String> wheres = new ArrayList<>();
        sb.append(" WHERE ");
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getDrillText().isEmpty()) continue;
            //If getDrillText is not empty, we need to add an inner join on the "previous" element in the dimension hierarchy
            //get the current index
            int index = dimensions.get(vd.getViewDimension()).indexOf(vd.getComboBoxText());
            //if index is two below hierarchy, we're trying to roll down from all, so we ignore. However, if index is 0, we can safely add a where
            if(index == dimensions.get(vd.getViewDimension()).size()-2) continue;
            if(previousDrillWheres.containsKey(vd.getViewDimension())){
                previousDrillWheres.remove(vd.getViewDimension());
            }
            String fromLevel = dimensions.get(vd.getViewDimension()).get(index+1);
            sb.append(fromLevel).append(" = '").append(vd.getDrillText()).append("'");
            currentDrillWheres.put(vd.getViewDimension(),sb.toString());
            wheres.add(sb.toString());
            sb = new StringBuilder();
        }
        for(String s : wheres){
            sb.append(s).append(" AND ");
        }
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String drillInnerJoin(){
        StringBuilder sb = new StringBuilder();
        ArrayList<String> joins = new ArrayList<>();
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getDrillText().isEmpty()) continue;
            //If getDrillText is not empty, we need to add an inner join on the "previous" element in the dimension hierarchy
            //get the current index
            int index = dimensions.get(vd.getViewDimension()).indexOf(vd.getComboBoxText());
            //if index is two below hierarchy, we're trying to roll down from all, so we ignore.
            if(index == dimensions.get(vd.getViewDimension()).size()-2) continue;
            //check if we just drilled down on the same dimension
            if(previousDrillJoins.containsKey(vd.getViewDimension())){
                //remove the key value pair
                previousDrillJoins.remove(vd.getViewDimension());
            }
            //else we add the inner join
            //declare dimension level that we drilled from
            String fromLevel = dimensions.get(vd.getViewDimension()).get(index+1);
            sb.append(" inner join cube.").append(fromLevel).append(" on view.").append(fromLevel)
                    .append("id = ").append("cube.").append(fromLevel).append(".").append(fromLevel).append("id ");
            currentDrillJoins.put(vd.getViewDimension(),sb.toString());
            joins.add(sb.toString());
            sb = new StringBuilder();

        }
        for(String s : joins){
            sb.append(s);
        }

        return sb.toString();
    }
    private void updateComboBoxes(){
        //this method updates all combo boxes we need to drill down on
        for(ViewDimension vd : this.viewDimensions){
            //If drillTextField is not empty, we need to update the combobox. If we roll down from all, ignore as well
            if(vd.getDrillText().isEmpty() || vd.getComboBoxText().equals("none")){
                continue;
            }
            //get index of curr level
            int index = dimensions.get(vd.getViewDimension()).indexOf(vd.getComboBoxText());
            //if index is 0, we skip todo throw an error
            if(index==0)
                continue;
            vd.getComboBox().setValue(dimensions.get(vd.getViewDimension()).get(index-1));
        }
    }
    public void drillDown() {
        updateComboBoxes();
        isDrillDown = true;
        loadView();
        /*
        try {

            getComboboxValues();

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


            if (!txtDrillTopic.getText().isEmpty())
                whereQuery = modifyDrillQuery(isWhere, whereQuery, topicDimension, txtDrillTopic.getText(), strComboTopic, comboTopic, indexTopic);
            else
                viewName += strComboTopic;


            if (!txtDrillLocation.getText().isEmpty())
                whereQuery = modifyDrillQuery(isWhere, whereQuery, locationDimension, txtDrillLocation.getText(), strComboLocation, comboLocation, indexLocation);
            else
                viewName += strComboLocation;


            if (!txtDrillDate.getText().isEmpty())
                whereQuery = modifyDrillQuery(isWhere, whereQuery, dateDimension, txtDrillDate.getText(), strComboDate, comboDate, indexDate);
            else
                viewName += strComboDate;


            if (!txtDrillOpinion.getText().isEmpty())
                whereQuery = modifyDrillQuery(isWhere, whereQuery, opinionDimension, txtDrillOpinion.getText(), strComboOpinion, comboOpinion, indexOpinion);
            else
                viewName += strComboOpinion;

            String locationInnerJoin = "";
            String dateInnerJoin = "";
            String opinionInnerJoin = "";
            String topicInnerJoin = "";

            if (!innerJoinQueryLocationFirst.isEmpty())
                locationInnerJoin = innerJoinQueryLocationFirst + " view" + innerJoinQueryLocationSecond;
            if (!innerJoinQueryDateFirst.isEmpty())
                dateInnerJoin = innerJoinQueryDateFirst + " view"+ innerJoinQueryDateSecond;
            if (!innerJoinQueryOpinionFirst.isEmpty())
                opinionInnerJoin = innerJoinQueryOpinionFirst + " view" + innerJoinQueryOpinionSecond;
            if (!innerJoinQueryTopicFirst.isEmpty())
                topicInnerJoin = innerJoinQueryTopicFirst + " view" + innerJoinQueryTopicSecond;
            System.out.println(select + selectStartQuery() + from + viewName + locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery);
            lastQuery = select + selectStartQuery() + from + viewName+ " view "+ locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery;
            lastQuery = orderByQuery(lastQuery);
            ResultSet resultSet = null;
            resultSet = ConnectionManager.selectSQL(lastQuery);
            buildTable(resultSet);
             popularityManager.updatePopularityValue(getCurrView());
            clearTextfields();

        } catch (NullPointerException e) {
            System.out.println("Choose dimensions");
        } catch (PSQLException e) {
            System.out.println("Invalid input in drill");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("you cant drill anymore");
        }
        resetGlobalVariables();
         */
    }

    private String selectStartQuery() {
        String selectColums = "";

        try {
            getComboboxValues();
            if (!(strComboTopic.isEmpty() || strComboTopic.equals("all"))) {
                selectColums = strComboTopic + ",";
            }
            if (!(strComboLocation.isEmpty() || strComboLocation.equals("all"))) {
                selectColums += strComboLocation + ",";
            }
            if (!(strComboDate.isEmpty() || strComboDate.equals("all"))) {
                selectColums += strComboDate + ",";
            }
            if (!(strComboOpinion.isEmpty() || strComboOpinion.equals("all"))) {
                selectColums += strComboOpinion + ",";
            }


        } catch (NullPointerException e) {
            System.out.println("choose Something");
        }
        String sumorCount = getViewNameSumOrCountMap().get(getCurrView());
        if (sumorCount.equals("sum"))
            return selectColums + "sum";
        else
            return selectColums + "count";

    }

    public void resetGlobalVariables() {
        viewName = "";

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

    public void slice() {
        String[] sliceCollums = txtSliceCollum.getText().split(",");
        String[] sliceValues = txtSliceValue.getText().split(",");

        try {
            String sliceQuery;
            if (lastQuery.contains("ORDER"))
                lastQuery = StringUtils.substringBefore(lastQuery, "ORDER");

            if (lastQuery.contains("WHERE"))
                sliceQuery = lastQuery + " AND( " + generateMultipleSliceWhereStatements(sliceCollums, sliceValues);
            else
                sliceQuery = lastQuery + " WHERE ( " + generateMultipleSliceWhereStatements(sliceCollums, sliceValues);
            popularityManager.updatePopularityValue(getCurrView());
            System.out.println("Slice:" + orderByQuery(sliceQuery));
            resultSet = ConnectionManager.selectSQL(orderByQuery(sliceQuery));

            buildTable(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("nothing can be sliced");
        }
    }

    private String generateMultipleSliceWhereStatements(String[] colums, String[] values) {
        int n = colums.length;
        String query = "";
        for (int i = 0; i < n; i++) {
            query += " " + "view" + "." + colums[i] + " = " + "'" + values[i] + "'";
            if (i != n - 1) {
                System.out.println(colums[i] + colums[i + 1]);
                if (colums[i].equals(colums[i + 1]))
                    query += " OR ";
                else
                    query += ") AND (";
            }

        }
        return query + ")";
    }

    public void locationRollUp() {
        rollUp("location");
    }

    public void opinionRollUp() {
        rollUp("opinion");
    }

    public void dateRollUp() {
        rollUp("date");
    }

    public void topicRollUp() {
        rollUp("topic");
    }


    private void rollUp(String dimension) {
        getComboboxValues();
        int variable;
        String viewName;
        try {
            if (dimension.equals("topic")) {
                alterViewName("topic");
            } else if (dimension.equals("location")) {
                alterViewName("location");
            } else if (dimension.equals("date")) {
                alterViewName("date");
            } else if (dimension.equals("opinion")) {
                alterViewName("opinion");
            }
            loadView();
            clearTextfields();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Not rollupable");
        } catch (NullPointerException e) {
            System.out.println("Du skal vælge");
        }
    }

    private void getComboboxValues() throws NullPointerException {
        strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
        strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
        strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
        strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();
    }

    private void alterViewName(String dimension) throws IndexOutOfBoundsException {
        String viewName;
        int dimensionIndex;
        if (dimension.equals("topic")) {
            dimensionIndex = topicDimension.indexOf(strComboTopic);
            comboTopic.setValue(topicDimension.get(dimensionIndex + 1));
        } else if (dimension.equals("location")) {
            dimensionIndex = locationDimension.indexOf(strComboLocation);
            comboLocation.setValue(locationDimension.get(dimensionIndex + 1));
        } else if (dimension.equals("date")) {
            dimensionIndex = dateDimension.indexOf(strComboDate);
            comboDate.setValue(dateDimension.get(dimensionIndex + 1));
        } else {
            dimensionIndex = opinionDimension.indexOf(strComboOpinion);
            comboOpinion.setValue(opinionDimension.get(dimensionIndex + 1));
        }
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

}
