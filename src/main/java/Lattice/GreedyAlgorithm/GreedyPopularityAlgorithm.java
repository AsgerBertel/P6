package Lattice.GreedyAlgorithm;

import Lattice.GraphManager;
import Lattice.Node;
import OLAP.NodeQueryUtils;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GreedyPopularityAlgorithm extends GreedyAlgorithm {
    private double scale = 1;

    public GreedyPopularityAlgorithm(GraphManager gm) {
        super(gm);
    }

    @Override
    public void updateCurrentBenefit() {
        try {
            updateBenefitScale();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.updateCurrentBenefit();
    }

    private void updateBenefitScale() throws SQLException {
        LinkedHashMap<Node, Double> sortedNodePopularityMap = new LinkedHashMap<>();
        LinkedHashMap<Node, Double> unsortedNodePopularityMap = ViewManager.getNodeReferencesFromViews(this.fillNodePopularityMap(), this.gm.nodes.keySet());
        unsortedNodePopularityMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> sortedNodePopularityMap.put(entry.getKey(), entry.getValue()));

        assignScalingToNodes(sortedNodePopularityMap);

        /**
         * Lav en sorteret liste af nodes baseret på popularitet - Lavest --> Højest
         * Assign scaling til hver node. Baseret på hvor stor forskel der er på current node og next nodes popularitet
         *
         */
    }

    private void assignScalingToNodes(LinkedHashMap<Node, Double> sortedNodePopularityMap) {
        double previousPopularityVal = 0;
        double maxVal = sortedNodePopularityMap.get(sortedNodePopularityMap.keySet().toArray()[sortedNodePopularityMap.size() - 1]);
        for (Node n : sortedNodePopularityMap.keySet()) {
            double currentPopularityValue = sortedNodePopularityMap.get(n);
            if (currentPopularityValue == 0) {
                n.setScale(scale);
                previousPopularityVal = currentPopularityValue;
            } else if (currentPopularityValue == previousPopularityVal) {
                n.setScale(scale);
            } else {
                double relativeDifference = (currentPopularityValue / maxVal) - (previousPopularityVal / maxVal);
                scale = (1 + relativeDifference) * scale;
                n.setScale(scale);
                previousPopularityVal = currentPopularityValue;
            }
        }
        scale = 1;
    }

    private int getAccumulatedGlobalPopularityValue() throws SQLException{
        ResultSet resultSet= ConnectionManager.selectSQL(QueryManager.getAccumulatedGlobalValue);
        resultSet.next();
        return resultSet.getInt(1);
    }
    private int getAccumulatedDailyPopularityValue(int maxDay) throws SQLException{
        ResultSet resultSet= ConnectionManager.selectSQL(QueryManager.getAccumulatedDailyValue(maxDay));
        resultSet.next();
        return resultSet.getInt(1);
    }

    private int getCurrentDay() throws SQLException {
        ResultSet rs = ConnectionManager.selectSQL("SELECT day, currentday FROM cube.popularity");
        int maxDay = 0;
        while(rs.next()){
            if(rs.getBoolean(2)){
                return rs.getInt(1);
            }
            if(rs.getInt(1) > maxDay)
                maxDay = rs.getInt(1);
        }
        return maxDay;
    }


    private LinkedHashMap<String, Double> fillNodePopularityMap() throws SQLException {
        LinkedHashMap<String, Double> viewPopularityMap = new LinkedHashMap<>();
        ResultSet resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopularity);
        ArrayList<ViewPopularity> listOfViews = new ArrayList<>();
        int curr_day = getCurrentDay();
        int dailyAccumulated = getAccumulatedDailyPopularityValue(curr_day);
        int globalAccumulated = getAccumulatedGlobalPopularityValue();
        while (resultSet.next()) {
            ViewPopularity view = new ViewPopularity(resultSet.getString(1), dailyAccumulated, globalAccumulated);
            if (listOfViews.contains(view)) {
                view = listOfViews.get(listOfViews.indexOf(view));
                view.incrementAmountOfTimesUsed();
                if (resultSet.getInt(3)==curr_day){
                    view.setCurrentDailyValue(resultSet.getInt(2));
                }
                view.setOverAllValue(resultSet.getInt(2));

            } else {
                view.incrementAmountOfTimesUsed();
                if (resultSet.getInt(3)==curr_day){
                    view.setCurrentDailyValue(resultSet.getInt(2));
                }
                view.setOverAllValue(resultSet.getInt(2));
                listOfViews.add(view);
            }
        }
        for (ViewPopularity v : listOfViews) {
            viewPopularityMap.put(v.getName(), v.calculatePopularityValue());
        }
        for (Node n : this.gm.nodes.keySet()) {
            if (!viewPopularityMap.containsKey(NodeQueryUtils.getNodeViewName(n))) {
                viewPopularityMap.put(NodeQueryUtils.getNodeViewName(n),0.0);
            }
        }
        ConnectionManager.updateSql(QueryManager.updateCurrentDayInPopularity);
        return viewPopularityMap;
    }


}
