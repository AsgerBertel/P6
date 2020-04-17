package Lattice;

import org.apache.spark.sql.catalyst.expressions.JsonTuple;

import java.util.*;

public class GraphManager {

    public HashMap<Node,Node> nodes = new HashMap<>();
    private Node curr_node;
    private Set<Node> doneNodes = new HashSet<>();

    Node topNode = new Node();

    public Node getTopNode() {
        return topNode;
    }

    //Generates children for every Node
    public void generateTree(Node n){
        curr_node = n;
        n.addChildren(generateChildren(n));
        for(Node child : n.getChildren()){
            if(!doneNodes.contains(child)){
                generateTree(child);
                doneNodes.add(child);
            }
        }
    }


    public void setTopNode(Node topNode) {
        this.topNode = topNode;
    }

    public Set<Node> getDoneNodes() {
        return doneNodes;
    }

    private ArrayList<Node> generateChildren(Node n) {
        HashMap<Dimension,ArrayList<Level>> dimensionLevelMap = new HashMap<>();
        //Fill strings - With Nodes dimension and its descendant in all spots.
        for(Dimension d: n.dimensions.keySet()){
            ArrayList<Level> arrayList = new ArrayList<>();
            arrayList.add(n.dimensions.get(d));
            try{
                Level descendant = d.getDescendant(n.dimensions.get(d));
                arrayList.add(descendant);
                dimensionLevelMap.put(d,arrayList);
            }catch (RuntimeException e){
                //get here if s is lowest level in the hierarchy. Just add s and nothing else.
                dimensionLevelMap.put(d,arrayList);
                //todo fix exception here when a custom has been made
            }
        }
        //calculate total number of rows needed to represent all strings
        int num_rows = 1;
        for(ArrayList<Level> l : dimensionLevelMap.values()){
            num_rows = num_rows*l.size();
        }
        ArrayList<LinkedHashMap<Dimension,Level>> big_daddy = new ArrayList<>();
        boolean isFirst = true;
        int previous = 0;
        for(Dimension ds : dimensionLevelMap.keySet()){
            ArrayList<Level> ls = dimensionLevelMap.get(ds);
            if(isFirst){
                previous = num_rows/ls.size();
                for(Level s : ls){
                    for(int i = 0; i < num_rows/ls.size();i++){
                        LinkedHashMap<Dimension,Level> map = new LinkedHashMap<>();
                        map.put(ds,s);
                        big_daddy.add(map);
                    }
                }
                isFirst = false;
            }
            else{
                previous = previous/ls.size();
                for(Level s : ls){
                    HashMap<String,Integer> duplicateMap = new HashMap<>();
                    for(LinkedHashMap<Dimension, Level> hm : big_daddy){
                        StringBuilder sb = new StringBuilder();
                        //combine all strings into one
                        for(Level keyString : hm.values()){
                            sb.append(keyString.getName());
                        }
                        //check if string is already in Set. If it is not, add s to map
                        if(!hm.containsKey(ds)){
                            if(duplicateMap.containsKey(sb.toString())){
                                if(duplicateMap.get(sb.toString()) < previous){
                                    hm.put(ds,s);
                                    duplicateMap.put(sb.toString(),duplicateMap.get(sb.toString())+1);
                                }
                            }
                            else{
                                hm.put(ds,s);
                                duplicateMap.put(sb.toString(),1);
                            }

                        }
                    }
                }
            }

        }
        //Get filtered list of nodes
        ArrayList<Node> filteredNodes = filterExistingNodes(ListOfMapsToNodes(big_daddy));
        for(Node node: filteredNodes){
            nodes.put(node,node);
        }
        //Add them to nodes map
        return filteredNodes;
    }

    private ArrayList<Node> filterExistingNodes(ArrayList<Node> list){
        ArrayList<Node> updatedList = new ArrayList<>();
        Iterator<Node> iterator = list.iterator();
        while(iterator.hasNext()){
            Node next = iterator.next();
            if(this.nodes.containsKey(next)){
                if(curr_node.equals(next)){
                    iterator.remove();
                }
            }
        }
        return list;
    }
    private ArrayList<Node> ListOfMapsToNodes(ArrayList<LinkedHashMap<Dimension,Level>> mapArrayList){
        ArrayList<Node> new_nodes = new ArrayList<>();
        for(LinkedHashMap<Dimension,Level> lhm : mapArrayList){
            Node new_node = new Node();
            for(Dimension val :lhm.keySet()){
                new_node.addDimension(lhm.get(val),val);
            }
            if(nodes.containsKey(new_node)){
                new_nodes.add(nodes.get(new_node));
            }else{
                new_nodes.add(new_node);
            }
        }
        return new_nodes;
    }

}
