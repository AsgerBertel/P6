package Lattice.GreedyAlgorithm;

import Lattice.Node;
import OLAP.NodeQueryUtils;

import java.util.LinkedHashMap;
import java.util.Set;

public class ViewManager {


    public static LinkedHashMap<Node, Integer> getNodeReferencesFromViews(LinkedHashMap<String, Integer> views, Set<Node> nodes){
        LinkedHashMap<Node, Integer> viewPopularityMap = new LinkedHashMap<>();
        LinkedHashMap<String, Node> viewNameNodeMap = getViewNameNodeMap(nodes);
        for(String s: views.keySet()){
            viewPopularityMap.put(viewNameNodeMap.get(s), views.get(s));
        }
        return viewPopularityMap;
    }
    private static LinkedHashMap<String, Node> getViewNameNodeMap(Set<Node> keyset){
        LinkedHashMap<String, Node> viewNameNodeMap = new LinkedHashMap<>();
        for(Node n: keyset){
            viewNameNodeMap.put(NodeQueryUtils.getNodeViewName(n),n);
        }
        return viewNameNodeMap;
    }
}
