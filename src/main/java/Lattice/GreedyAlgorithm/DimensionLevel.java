package Lattice.GreedyAlgorithm;

public class DimensionLevel {
    String currentDimensionLevel;
    String formerDimensionLevel;
    String nexDimensionLevel;
    int rows;

    public DimensionLevel(String currentDimensionLevel, String formerDimensionLevel, String nexDimensionLevel, int rows) {
        this.currentDimensionLevel = currentDimensionLevel;
        this.formerDimensionLevel = formerDimensionLevel;
        this.nexDimensionLevel = nexDimensionLevel;
        this.rows = rows;
    }

    public String getCurrentDimensionLevel() {
        return currentDimensionLevel;
    }

    public String getFormerDimensionLevel() {
        return formerDimensionLevel;
    }

    public String getNexDimensionLevel() {
        return nexDimensionLevel;
    }

    public int getRows() {
        return rows;
    }


}
