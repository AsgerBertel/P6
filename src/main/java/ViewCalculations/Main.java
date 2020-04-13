package ViewCalculations;


import opennlp.tools.parser.Parse;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Main {

    HashMap<String, Integer> listOfProductRows = new HashMap<>();
    HashMap<String, Integer> listOfLocationRows = new HashMap<>();
    HashMap<String, Integer> listOfDateRows = new HashMap<>();
    HashMap<String, Integer> listOfOpinionRows = new HashMap<>();
    HashMap<String, Integer> listOfAllRowCombinations;
    String fistSmallest;
    String secondSmallest;
    ArrayList<View> listOfViews = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public void run() {
        listOfLocationRows = calculateRowCombinations();
        for (String q : listOfLocationRows.keySet()) {
            //    System.out.println(q + " " + listOfAllRowCombinations.get(q));
        }
        System.out.println(listOfAllRowCombinations.size());
    }

    public HashMap calculateRowCombinations() {
        listOfAllRowCombinations = new HashMap<>();
        listOfProductRows.put("products", 40);
        listOfProductRows.put("category", 9);
        //  listOfProductRows.put("all", 40);

     //   listOfLocationRows.put("coordinates", 3861358);
        listOfLocationRows.put("district", 143);
        listOfLocationRows.put("county", 15);
        listOfLocationRows.put("city", 2);
        listOfLocationRows.put("country", 1);
        // listOfLocationRows.put("all", 3861358);

        listOfOpinionRows.put("Opinion", 3);
        // listOfOpinionRows.put("all", 3);

     //   listOfDateRows.put("day", 1116);
        listOfDateRows.put("month", 36);
        listOfDateRows.put("year", 3);
        // listOfDateRows.put("all", 1116);
        //combine product and location

        //combine all dimensions
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfOpinionRows.keySet()) {
                for (String p : listOfDateRows.keySet()) {
                    for (String l : listOfLocationRows.keySet()) {
                        listOfAllRowCombinations.put(s + "-" + q + "-" + p + "-" + l, listOfProductRows.get(q) * listOfOpinionRows.get(s) * listOfDateRows.get(p) * listOfLocationRows.get(l));

                        System.out.println(s + "-" + q + "-" + p + "-" + l +  " " + listOfProductRows.get(q) * listOfOpinionRows.get(s) * listOfDateRows.get(p) * listOfLocationRows.get(l));

                    /*    View view = new View();
                        BigInteger bigInteger = BigInteger.valueOf(listOfProductRows.get(q) * listOfOpinionRows.get(s) * listOfDateRows.get(p) * listOfLocationRows.get(l));
                        view.setRows(bigInteger);
                        view.setVievName(s + "-" + q + "-" + p + "-" + l);
                        view.addToListOfViewChildren(s);
                        view.addToListOfViewChildren(q);
                        view.addToListOfViewChildren(p);
                        view.addToListOfViewChildren(l);
                        listOfViews.add(view);*/
                    }
                }
            }
        }

       /* Collections.sort(listOfViews, new CustomComparater());
        for (View view : listOfViews) {
            System.out.println(view.vievName + view.getRows());
            for (String s : view.getListOfViewChildren()) {
                System.out.println(s);
            }
            System.out.println("-----------------------------");
        }*/

        return listOfAllRowCombinations;
    }
}
