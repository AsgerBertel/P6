package Lattice;

import scala.tools.nsc.Global;

import java.util.ArrayList;
import java.util.Arrays;

public class Dimension {

    ArrayList<String> hierarchy = new ArrayList<>();

    public String getAscendant(String s){
        if(this.hierarchy.contains(s)){
            int index = this.hierarchy.indexOf(s);
            if(index-1 >= 0){
                return this.hierarchy.get(index-1);
            }else{
                //todo implement custom exception
                throw new RuntimeException("Out of bounds");
            }
        }else{
            //todo implement custom exception
            throw new RuntimeException("String not in hierarchy");
        }
    }
    public String getDescendant(String s){
        if(this.hierarchy.contains(s)){
            int index = this.hierarchy.indexOf(s);
            if(index+1 <= this.hierarchy.size()-1){
                return this.hierarchy.get(index+1);
            }else{
                //todo implement custom exception
                throw new RuntimeException("Out of bounds");
            }
        }else{
            //todo implement custom exception
            throw new RuntimeException("String not in hierarchy");
        }
    }

    public Dimension(String[] array){
        this.hierarchy.addAll(Arrays.asList(array));
    }
}
