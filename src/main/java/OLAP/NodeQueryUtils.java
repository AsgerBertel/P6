package OLAP;

import Lattice.Dimension;
import Lattice.Level;
import Lattice.Node;



public class NodeQueryUtils {

    public static String getNodeViewName(Node node){
        StringBuilder sb = new StringBuilder();
        for(Dimension d: node.getDimensions().keySet()){
            sb.append(node.getDimensions().get(d).getName());
        }
        return sb.toString();
    }

}
