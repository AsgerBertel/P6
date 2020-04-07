package Lattice.GreedyAlgorithm;

import java.util.ArrayList;

public class Main {
    ArrayList<DimensionLevel> productDimension = new ArrayList<>();
    ArrayList<DimensionLevel> dateDimension = new ArrayList<>();

    ArrayList<DimensionLevel> opinionDimension = new ArrayList<>();

    ArrayList<DimensionLevel> locationDimension = new ArrayList<>();


    public static void main(String[] args) {
        Main main = new Main();
        main.calculateRelations();
    }


    private void calculateRelations() {

        productDimension.add(new DimensionLevel("products", "none", "category", 250000));
        productDimension.add(new DimensionLevel("category", "products", "all", 10000));
        productDimension.add(new DimensionLevel("all", "category", "none", 250000));


        dateDimension.add(new DimensionLevel("day", "none", "month", 10000));
        dateDimension.add(new DimensionLevel("month", "none", "year", 10000));
        dateDimension.add(new DimensionLevel("year", "month", "all", 10000));
        dateDimension.add(new DimensionLevel("all", "year", "none", 10000));

        Cuboid cuboid = new Cuboid();
        cuboid.addDimensionLevel(productDimension.get(0));
        cuboid.addDimensionLevel(dateDimension.get(0));
        System.out.println(cuboid.getName() + cuboid.getRows());


    }
}
