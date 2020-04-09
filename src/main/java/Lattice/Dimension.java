package Lattice;


import java.util.ArrayList;
import java.util.Arrays;

public class Dimension {

    ArrayList<Level> hierarchy = new ArrayList<>();

    public Level getAscendant(Level l){
        if(this.hierarchy.contains(l)){
            int index = this.hierarchy.indexOf(l);
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
    public int getLevelIndex(Level l){
        return this.hierarchy.indexOf(l);
    }

    public Level getLevel(int levelIndex){
        return this.hierarchy.get(levelIndex);
    }
    public Level getDescendant(Level l){
        if(this.hierarchy.contains(l)){
            int index = this.hierarchy.indexOf(l);
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
    /**
     * Gets all descendants of a given string, excluding the given string
     * **/
    public ArrayList<Level> getAllDescendants(Level l){
        ArrayList<Level> descendants = new ArrayList<>();
        int i = hierarchy.indexOf(l);
        while(true){
            try{
                descendants.add(getDescendant(hierarchy.get(i++)));
            }catch (RuntimeException e){
                System.out.println(e.toString());
                return descendants;
            }
        }
    }
    /**
     * Gets all descendants of a given string, including the given string
     * **/
    public ArrayList<Level> getAllDescendantsAndPassed(Level l){
        ArrayList<Level> descendants = new ArrayList<>();
        descendants.add(l);
        descendants.addAll(getAllDescendants(l));
        return descendants;
    }

    public Dimension(Level[] array) {
        this.hierarchy.addAll(Arrays.asList(array));
    }


    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        Dimension cmp_d = (Dimension) obj;
        return hierarchy.equals(cmp_d.hierarchy);
    }
}
