package ViewCalculations;

import java.util.Comparator;

public class CustomComparater implements Comparator<View> {
    @Override
    public int compare(View o1, View o2) {
        return o1.getRows().compareTo(o2.getRows());
    }
}
