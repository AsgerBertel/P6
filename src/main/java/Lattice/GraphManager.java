package Lattice;

import org.apache.spark.sql.catalyst.expressions.JsonTuple;

import java.util.*;

public class GraphManager {

    private HashMap<Node,Node> nodes = new HashMap<>();
    private Node curr_node;
    private Set<Node> doneNodes = new HashSet<>();
    public static void main(String[] args) {
        GraphManager gm = new GraphManager();
        gm.lol();
    }
    public void lol(){
        Dimension d1 = new Dimension(new String[]{"Prod","Category","None"});
        Dimension d2 = new Dimension(new String[]{"Location", "District","County","City","Country","None"});
        Dimension d3 = new Dimension(new String[]{"Day","Month","Year","None"});
        Dimension d4 = new Dimension(new String[]{"Opinion","None"});
        //Create the root node
        Node n = new Node(new Object[][]{
                {d1,"Prod"},
                {d2,"Location"},
                {d3, "Day"},
                {d4, "Opinion"}
        });
        this.nodes.put(n,n);
        generateTree(n);
        System.out.println(nodes.size());
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

    private ArrayList<Node> generateChildren(Node n) {
        HashMap<Dimension,ArrayList<String>> strings = new HashMap<>();
        //Fill strings - With Nodes dimension and its descendant in all spots.
        for(Dimension d: n.dimensions.keySet()){
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(n.dimensions.get(d));
            try{
                String descendant = d.getDescendant(n.dimensions.get(d));
                arrayList.add(descendant);
                strings.put(d,arrayList);
            }catch (RuntimeException e){
                //get here if s is lowest level in the hierarchy. Just add s and nothing else.
                strings.put(d,arrayList);
                //todo fix exception here when a custom has been made
            }
        }
        //calculate total number of rows needed to represent all strings
        int num_rows = 1;
        for(ArrayList<String> l : strings.values()){
            num_rows = num_rows*l.size();
        }
        ArrayList<LinkedHashMap<Dimension,String>> big_daddy = new ArrayList<>();
        boolean isFirst = true;
        int previous = 0;
        for(Dimension ds : strings.keySet()){
            ArrayList<String> ls = strings.get(ds);
            if(isFirst){
                previous = num_rows/ls.size();
                for(String s : ls){
                    for(int i = 0; i < num_rows/ls.size();i++){
                        LinkedHashMap<Dimension,String> map = new LinkedHashMap<>();
                        map.put(ds,s);
                        big_daddy.add(map);
                    }
                }
                isFirst = false;
            }
            else{
                previous = previous/ls.size();
                for(String s : ls){
                    HashMap<String,Integer> duplicateMap = new HashMap<>();
                    for(LinkedHashMap<Dimension, String> hm : big_daddy){
                        StringBuilder sb = new StringBuilder();
                        //combine all strings into one
                        for(String keyString : hm.values()){
                            sb.append(keyString);
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
    private ArrayList<Node> ListOfMapsToNodes(ArrayList<LinkedHashMap<Dimension,String>> mapArrayList){
        ArrayList<Node> new_nodes = new ArrayList<>();
        for(LinkedHashMap<Dimension,String> lhm : mapArrayList){
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
