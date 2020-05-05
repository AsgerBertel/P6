package GUI.Controller;

import GUI.ContextMenu.SearchBarContextMenu;
import GUI.Other.PopularityManager;
import Lattice.GreedyAlgorithm.GreedyAlgorithmType;
import OLAP.ViewGenerator;
import Sql.ConnectionManager;
import Sql.QueryManager;
import ViewCalculations.View;
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
import org.apache.spark.sql.execution.columnar.NULL;
import org.postgresql.util.PSQLException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

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

    @FXML
    private ComboBox comboLocation, comboOpinion, comboTopic, comboDate;
    @FXML
    private TextField txtTopic, txtLocation, txtOpinion, txtDate, txtDrillDate, txtDrillOpinion, txtDrillLocation, txtDrillTopic, txtTopRows;
    @FXML
    private Button btnUpdate, btnSearch;
    @FXML
    private TableView tableViewLeft;
    @FXML
    private Label lblViewSize;

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
        locationDimension.addAll(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "all"));
        dateDimension.addAll(FXCollections.observableArrayList("day", "month", "year", "all"));
        topicDimension.addAll(FXCollections.observableArrayList("toptopic", "all"));
        opinionDimension.addAll(FXCollections.observableArrayList("opinion", "all"));

        searchBarContextMenu.setStyle("-fx-pref-width: 200");
        comboLocation.setItems(FXCollections.observableArrayList("coordinate", "district", "county", "city", "country", "all"));
        comboOpinion.setItems(FXCollections.observableArrayList("opinion", "all"));
        comboTopic.setItems(FXCollections.observableArrayList("toptopic", "all"));
        comboDate.setItems(FXCollections.observableArrayList("day", "month", "year", "all"));
        initializeTopicOnKeyPressedEvent(comboTopic);
        initializeOpinionOnKeyPressedEvent(comboOpinion);
        initializeLocationOnKeyPressedEvent(comboLocation);
        initializeateDateOnKeyPressedEvent(comboDate);
        initializeOnlyNumericTextfield(txtTopRows);
        tableViewLeft.setItems(data);

        //ViewDimensions ORDER IS VERY IMPORTANT
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.TOPIC, comboTopic, txtDrillTopic, txtTopic));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.LOCATION, comboLocation, txtDrillLocation, txtLocation));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.DATE, comboDate, txtDrillDate, txtDate));
        viewDimensions.add(new ViewDimension(ViewDimensionEnum.OPINION, comboOpinion, txtDrillOpinion, txtOpinion));
    }
    public void clearTextfields(){
        txtTopic.clear();
        txtLocation.clear();
        txtOpinion.clear();
        txtDate.clear();
        txtDrillDate.clear();
        txtDrillOpinion.clear();
        txtDrillLocation.clear();
        txtDrillTopic.clear();
        txtTopRows.clear();
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

    public void buildTable(ResultSet resultSet_Tableview) throws SQLException {
        data.clear();
        tableViewLeft.getColumns().clear();
        tableViewLeft.getItems().clear();
        DecimalFormat df = new DecimalFormat("#.#####");
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
            ObservableList row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet_Tableview.getMetaData().getColumnCount(); i++) {
                try{
                    double number = Double.parseDouble(resultSet_Tableview.getString(i));
                    row.add(df.format(number));
                } catch (NumberFormatException e){
                    row.add(resultSet_Tableview.getString(i));
                }
            }
            data.add(row);
        }
        lblViewSize.setText("View size: "+ data.size());
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
    private String addSelectFrequencyDeviation(){
        StringBuilder sb = new StringBuilder();
        //sum or count
        String aggregate = getViewNameSumOrCountMap().get(getCurrView());
        sb.append(",avgval, (").append(aggregate).append("/avgval)*100 freqdev");
        return sb.toString();
    }

    private String addSelectOpinionDeviation(){
        return ", (percentofmentions/averagepercent)*100 odev";
    }

    private String addMeasuresToSelect(){
        StringBuilder sb = new StringBuilder();
        //append to selects
        if(chkFD.isSelected()){
            sb.append(addSelectFrequencyDeviation());
        }
        if(chkOD.isSelected()){
            sb.append(addSelectOpinionDeviation());
        }
        if(sb.length()!=0)
            return sb.toString();
        return "";
    }
    private String addFrequencyDeviationInnerJoin(){
        //only requires topic to not be all.
        StringBuilder sb = new StringBuilder();
        //add inner join
        sb.append("inner join ");
        //select stmnt where we get the avg sum for given topics/opinions.
        sb.append("(SELECT ");
        for(ViewDimension vd : this.viewDimensions){
            //add all dimensions EXCEPT for location (to get average of all locations)
            if(vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //"avg(sum or count) avgval"
        sb.append("avg(").append(getViewNameSumOrCountMap().get(getCurrView())).append(") avgval FROM ").append("public.").append(getCurrView());
        //add group by
        sb.append(" GROUP BY ");
        for(ViewDimension vd : this.viewDimensions){
            //add all dimensions EXCEPT for location (to get average of all locations)
            if(vd.getViewDimension().equals(ViewDimensionEnum.LOCATION) || vd.getComboBoxText().equals("none"))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length()-2);
        //
        //add inner join conditions
        sb.append(") freqaverage on ");
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getViewDimension().equals(ViewDimensionEnum.LOCATION) || vd.getComboBoxText().equals("none"))
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

    private String addDeviationInnerJoins(){
        StringBuilder sb = new StringBuilder();
        if(chkFD.isSelected())
            sb.append(addFrequencyDeviationInnerJoin());
        if(chkOD.isSelected())
            //IF OPINION IS ALL THEN WE CAN NOT INCLUDE THIS
            sb.append(addOpinionDeviationInnerJoin());
        //Only edge case is if opinion is set to all, then we should not include
        return sb.toString();
    }

    private String addOpinionDeviationInnerJoin() {
        StringBuilder sb = new StringBuilder();
        //add local percent of mentions join
        sb.append(addOpinionDeviationPercentOfMentionsJoin());
        //add average global percent of mentions join
        sb.append(addOpinionDeviationAveragePercentOfMentionsJoin());
        return sb.toString();
    }

    private String addOpinionDeviationPercentOfMentionsJoin() {
        StringBuilder sb = new StringBuilder();
        sb.append(" inner join (SELECT ");
        //add selects
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none"))
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
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none"))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //add sum + total sum
        sb.append("sum, totalsum) percentsum on ");
        //add inner join conditions
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none"))
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
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
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
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length()-2);
        //add avgpercent and inner join conditions
        sb.append(") avgpercent on ");
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.LOCATION))
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
    private String addTotalSumInnerJoin(){
        StringBuilder sb = new StringBuilder();
        sb.append("( SELECT ");
        //Select
        for(ViewDimension vd : this.viewDimensions){
            //select everything except for opinion
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //append sum aggregate of sum or count
        sb.append("sum(").append(getViewNameSumOrCountMap().get(getCurrView())).append(") totalsum");
        sb.append(" from public.").append(getCurrView());
        //group by
        sb.append(" group by ");
        for(ViewDimension vd : this.viewDimensions){
            //select everything except for opinion
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append(vd.getComboBoxText()).append(", ");
        }
        //remove last comma
        sb.setLength(sb.length()-2);
        sb.append(") b on ");
        //add inner join conditions
        for(ViewDimension vd : this.viewDimensions){
            if(vd.getComboBoxText().equals("none") || vd.getViewDimension().equals(ViewDimensionEnum.OPINION))
                continue;
            sb.append("a.").append(vd.getComboBoxText()).append(" = b.").append(vd.getComboBoxText()).append(" AND ");
        }
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    private String getCurrView(){
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
        for(ViewDimension vd : viewDimensions){
            if(vd.getComboBoxText().equals("coordinate")){
                sb.append("view.lat, view.long,");
            }else{
                if(!vd.getComboBoxText().equals("none"))
                    sb.append("view.").append(vd.getComboBoxText()).append(",");
            }
        }
        sb.append("view.").append(getViewNameSumOrCountMap().get(viewName));
        //IF ANY MEASURES SELECTED, ADD MEASURES
        if(hasMeasures())
            sb.append(addMeasuresToSelect());
        sb.append(" FROM ").append(viewName).append(" view \n");
        return sb.toString();
    }

    private boolean hasMeasures() {
        return chkOD.isSelected() || chkFD.isSelected() || chkIPD.isSelected();
    }

    private boolean hasWhereContent() {
        for (ViewDimension vd : viewDimensions) {
            if (!vd.getWhereText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private String whereQuery() {
        //if all where textfields are empty, return empty string
        if (!hasWhereContent())
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" WHERE ");
        for (ViewDimension vd : viewDimensions) {
            if (!vd.getWhereText().isEmpty())
                sb.append(vd.getComboBoxText()).append(" = '").append(vd.getWhereText()).append("' AND ");
        }
        //remove last AND
        String s = sb.toString();
        if (s.substring(s.length() - 4).equals("AND ")) {
            s = s.substring(0, s.length() - 4);
        }
        return s;
    }

    public void loadView() {
        btnSearch.setDisable(false);
        try {
            String query = selectQuery() + whereQuery();
            //ADD INNER JOINS HERE
            query+=addDeviationInnerJoins();
            if (txtTopRows.getText().isEmpty()) {
                buildTable(ConnectionManager.selectSQL(query));
            } else {
                buildTable(ConnectionManager.selectSQL(orderByQuery(query)));
            }

        } catch (NullPointerException e) {
            System.out.println("Choose some");

        } catch (PSQLException e){
            System.out.println("This view does not exist");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            clearTextfields();
            btnSearch.setDisable(false);
        }
    }

    public String orderByQuery(String query) {
        query = query + " ORDER BY " + getViewNameSumOrCountMap().get(getCurrView()) + " LIMIT " + txtTopRows.getText();
        return query;
    }

    public void drillDown() {
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
                locationInnerJoin = innerJoinQueryLocationFirst + viewName + innerJoinQueryLocationSecond;
            if (!innerJoinQueryDateFirst.isEmpty())
                dateInnerJoin = innerJoinQueryDateFirst + viewName + innerJoinQueryDateSecond;
            if (!innerJoinQueryOpinionFirst.isEmpty())
                opinionInnerJoin = innerJoinQueryOpinionFirst + viewName + innerJoinQueryOpinionSecond;
            if (!innerJoinQueryTopicFirst.isEmpty())
                topicInnerJoin = innerJoinQueryTopicFirst + viewName + innerJoinQueryTopicSecond;
            System.out.println(selectQuery + viewName + locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery);
            ResultSet resultSet = null;

            resultSet = ConnectionManager.selectSQL(selectQuery + viewName + locationInnerJoin + dateInnerJoin + opinionInnerJoin + topicInnerJoin + whereQuery);
            buildTable(resultSet);
            clearTextfields();

        } catch(NullPointerException e){
            System.out.println("Choose dimensions");
        }
        catch (PSQLException e){
            System.out.println("Invalid input in drill");
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("you cant drill anymore");
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

    public void locationRollUp(){
        rollUp("location");
    }
    public void opinionRollUp(){
        rollUp("opinion");
    }
    public void dateRollUp(){
        rollUp("date");
    }
    public void topicRollUp(){
        rollUp("topic");
    }


    private void rollUp(String dimension){
        getComboboxValues();
        int variable;
        String viewName ;
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
        } catch(IndexOutOfBoundsException e){
            System.out.println("Not rollupable");
        }
    }
    private void getComboboxValues() throws NullPointerException{

        strComboTopic = comboTopic.getSelectionModel().getSelectedItem().toString();
        strComboOpinion = comboOpinion.getSelectionModel().getSelectedItem().toString();
        strComboLocation = comboLocation.getSelectionModel().getSelectedItem().toString();
        strComboDate = comboDate.getSelectionModel().getSelectedItem().toString();
    }
    private void alterViewName(String dimension) throws IndexOutOfBoundsException{
        String viewName;
        int dimensionIndex;
        if(dimension.equals("topic")){
            dimensionIndex = topicDimension.indexOf(strComboTopic);
            comboTopic.setValue(topicDimension.get(dimensionIndex+1));
        }else if(dimension.equals("location")){
            dimensionIndex = locationDimension.indexOf(strComboLocation);
            comboLocation.setValue(locationDimension.get(dimensionIndex+1));
        }else if(dimension.equals("date")){
            dimensionIndex = dateDimension.indexOf(strComboDate);
            comboDate.setValue(dateDimension.get(dimensionIndex+1));
        }else {
            dimensionIndex = opinionDimension.indexOf(strComboOpinion);
            comboOpinion.setValue(opinionDimension.get(dimensionIndex+1));
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
