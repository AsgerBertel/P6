package Lattice.GreedyAlgorithm;

import Lattice.Node;
import OLAP.NodeQueryUtils;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GreedyPopularityAlgorithm extends GreedyAlgorithm {
    private double scale = 1;
    LinkedHashMap<String, Integer> popularityNodeMap;

    public GreedyPopularityAlgorithm(Set<Node> nodes, Node rootNode) {
        super(nodes, rootNode);
        try {
            popularityNodeMap = FillNodePopularityMap(nodes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateCurrentBenefit() {
        updateBenefitScale();
        super.updateCurrentBenefit();
    }

    private void updateBenefitScale() {
        LinkedHashMap<Node, Integer> sortedNodePopularityMap = new LinkedHashMap<>();
        LinkedHashMap<Node, Integer> unsortedNodePopularityMap = ViewManager.getNodeReferencesFromViews(popularityNodeMap, nodes);
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

    private void assignScalingToNodes(LinkedHashMap<Node, Integer> sortedNodePopularityMap) {
        double previousPopularityVal = 0;
        double maxVal = sortedNodePopularityMap.get(sortedNodePopularityMap.keySet().toArray()[sortedNodePopularityMap.size() - 1]);
        for (Node n : sortedNodePopularityMap.keySet()) {
            int currentPopularityValue = sortedNodePopularityMap.get(n);
            if (currentPopularityValue == 30)
                System.out.println("i like trains");
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

    private LinkedHashMap<String, Integer> FillNodePopularityMap(Set<Node> setOfNodes) throws SQLException {
        LinkedHashMap<String, Integer> viewPopularityMap = new LinkedHashMap<>();
        ResultSet resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopularity);
        ArrayList<ViewPopularity> listOfViews = new ArrayList<>();
        while (resultSet.next()) {
            ViewPopularity view = new ViewPopularity(resultSet.getString(1));
            if (listOfViews.contains(view)) {
                view = listOfViews.get(listOfViews.indexOf(view));
                view.incrementAmountOfTimesUsed();
                if (resultSet.getBoolean(4))
                    view.setCurrentDailyValue(resultSet.getInt(2));
                else
                    view.setOverAllValue(resultSet.getInt(2));

            } else {
                view.incrementAmountOfTimesUsed();
                if (resultSet.getBoolean(4))
                    view.setCurrentDailyValue(resultSet.getInt(2));
                else
                    view.setOverAllValue(resultSet.getInt(2));

                listOfViews.add(view);
            }
        }
        for (ViewPopularity v : listOfViews) {
            viewPopularityMap.put(v.getName(), v.calculatePopularityValue());
        }
        for (Node n : nodes) {
            if (!viewPopularityMap.containsKey(NodeQueryUtils.getNodeViewName(n))) {
                viewPopularityMap.put(NodeQueryUtils.getNodeViewName(n),0);
            }
        }
        ConnectionManager.updateSql(QueryManager.updateCurrentDayInPopularity);
        return viewPopularityMap;
    }


}
