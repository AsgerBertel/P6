package ViewCalculations;

import java.math.BigInteger;
import java.util.ArrayList;

public class View {
    String vievName;
    BigInteger rows;
    ArrayList<String> listOfViewChildren = new ArrayList<>();

    public String getVievName() {
        return vievName;
    }

    public BigInteger getRows() {
        return rows;
    }

    public ArrayList<String> getListOfViewChildren() {
        return listOfViewChildren;
    }

    public void setVievName(String vievName) {
        this.vievName = vievName;
    }

    public void setRows(BigInteger rows) {
        this.rows = rows;
    }

    public void addToListOfViewChildren(String viewChild) {
        listOfViewChildren.add(viewChild);
    }
}
