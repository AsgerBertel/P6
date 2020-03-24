package Sql;

public class FactTable {
    private int dayID;
    private int locationID;
    private int opinionID;
    private int productID;

    public FactTable(int dayID, int locationID, int opinionID, int productID) {
        this.dayID = dayID;
        this.locationID = locationID;
        this.opinionID = opinionID;
        this.productID = productID;
    }

}
