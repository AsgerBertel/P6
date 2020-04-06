package Lattice;

import java.util.*;

public class TreeManager {

    private HashMap<Node,Node> nodes = new HashMap<>();
    public static void main(String[] args) {
        TreeManager tm = new TreeManager();
        tm.lol();
    }
    public void lol(){
        Dimension d1 = new Dimension(new String[]{"Prod","Cat"});
        Dimension d2 = new Dimension(new String[]{"Loc","County"});
        //Create the root node
        Node n = new Node(new Object[][]{
                {"Prod",d1},
                {"Loc",d2}
        });
        this.nodes.put(n,n);
        generateTree(n);
    }

    //Generates children for every Node
    public void generateTree(Node n){
        n.addChildren(generateChilden(n));
        for(Node child : n.getChildren()){
            generateTree(child);
        }
    }

    private ArrayList<Node> generateChilden(Node n) {
        HashMap<ArrayList<String>,Dimension> strings = new HashMap<>();
        //Fill strings - With Nodes dimension and its descendant in all spots.
        for(String s: n.dimensions.keySet()){
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(s);
            try{
                String descendant = n.dimensions.get(s).getDescendant(s);
                arrayList.add(descendant);
                strings.put(arrayList, n.dimensions.get(s));
            }catch (RuntimeException e){
                //get here if s is lowest level in the hierarchy. Just add s and nothing else.
                strings.put(arrayList,n.dimensions.get(s));
                //todo fix exception here when a custom has been made
            }
        }
        //calculate total number of rows needed to represent all strings
        int num_rows = 1;
        for(ArrayList<String> l : strings.keySet()){
            num_rows = num_rows*l.size();
        }
        ArrayList<LinkedHashMap<String,Dimension>> big_daddy = new ArrayList<>();
        boolean isFirst = true;
        int pow_count = 0;
        for(ArrayList<String> ls : strings.keySet()){
            pow_count++;
            if(isFirst){
                for(String s : ls){
                    for(int i = 0; i < num_rows/ls.size();i++){
                        LinkedHashMap<String,Dimension> map = new LinkedHashMap<>();
                        map.put(s,strings.get(ls));
                        big_daddy.add(map);
                    }
                }
                isFirst = false;
            }
            else{
                for(String s : ls){
                    HashMap<String,Integer> duplicateMap = new HashMap<>();
                    for(LinkedHashMap<String, Dimension> hm : big_daddy){
                        StringBuilder sb = new StringBuilder();
                        //combine all strings into one
                        for(String keyString : hm.keySet()){
                            sb.append(keyString);
                        }
                        //check if string is already in Set. If it is not, add s to map
                        if(!hm.containsValue(strings.get(ls))){
                            if(duplicateMap.containsKey(sb.toString())){
                                if(duplicateMap.get(sb.toString()) < num_rows/Math.pow(ls.size(),pow_count)){
                                    hm.put(s,strings.get(ls));
                                    duplicateMap.put(sb.toString(),duplicateMap.get(sb.toString())+1);
                                }
                            }
                            else{
                                hm.put(s,strings.get(ls));
                                duplicateMap.put(sb.toString(),1);
                            }

                        }
                    }
                }
            }

        }
        //todo change return
        return null;
    }

}
