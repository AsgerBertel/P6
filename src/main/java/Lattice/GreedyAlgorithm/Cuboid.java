package Lattice.GreedyAlgorithm;

import java.util.ArrayList;

public class Cuboid {
    private ArrayList<DimensionLevel> listOfDimensionLevels = new ArrayList<>();
    int rows;
    String name = "";


    public void addDimensionLevel(DimensionLevel dimensionLevel) {
        listOfDimensionLevels.add(dimensionLevel);
        calculateName();
        calculateRows();
    }

    public ArrayList<DimensionLevel> getListOfDimensionLevels() {
        return listOfDimensionLevels;
    }

    private void calculateName() {
        for (DimensionLevel d : listOfDimensionLevels) {
            StringBuilder sb2 = new StringBuilder(name);
            sb2.append("-"+d.getCurrentDimensionLevel());
            name = sb2.toString();
        }
    }

    private void calculateRows() {

        for (DimensionLevel d : listOfDimensionLevels) {
            if (d == listOfDimensionLevels.get(0))
                rows = d.getRows();
            else
            rows = d.getRows();
        }
    }

    public int getRows() {
        return rows;
    }

    public String getName() {
        return name;
    }
}
