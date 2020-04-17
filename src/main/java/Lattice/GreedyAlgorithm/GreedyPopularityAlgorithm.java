package Lattice.GreedyAlgorithm;

import Lattice.Node;
import OLAP.NodeQueryUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GreedyPopularityAlgorithm extends GreedyAlgorithm {
    private double scale = 1;
    LinkedHashMap<String, Integer> popularityNodeMap;

    public GreedyPopularityAlgorithm(Set<Node> nodes, Node rootNode) {
        super(nodes, rootNode);
        popularityNodeMap = testFillNodePopularityMap();
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
            if (previousPopularityVal == 0) {
               n.setScale(scale);
               previousPopularityVal = currentPopularityValue;
            } else if(currentPopularityValue == previousPopularityVal){
                n.setScale(scale);
            } else{
                double relativeDifference = (currentPopularityValue / maxVal) - (previousPopularityVal / maxVal);
                scale = (1 + relativeDifference) * scale;
                n.setScale(scale);
                previousPopularityVal = currentPopularityValue;
            }
        }
        scale = 1;
    }


    private LinkedHashMap<String, Integer> testFillNodePopularityMap(){
        Random r = new Random();
        LinkedHashMap<String, Integer> testMap = new LinkedHashMap<>();
        for(Node n: nodes){
            int tempRandom = r.nextInt(1000);
            testMap.put(NodeQueryUtils.getNodeViewName(n),tempRandom);
            System.out.println(NodeQueryUtils.getNodeViewName(n) + " : " + tempRandom);
        }

        return testMap;
    }


}
