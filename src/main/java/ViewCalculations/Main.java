package ViewCalculations;

import java.util.HashMap;

public class Main {

    HashMap<String, Integer> listOfProductRows = new HashMap<>();
    HashMap<String, Integer> listOfLocationRows = new HashMap<>();
    HashMap<String, Integer> listOfDateRows = new HashMap<>();
    HashMap<String, Integer> listOfOpinionRows = new HashMap<>();
    HashMap<String, Integer> listOfAllRowCombinations;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();


    }

    public void run() {
        listOfLocationRows = calculateRowCombinations();
        for (String q : listOfLocationRows.keySet()) {
            System.out.println(q + " " + listOfAllRowCombinations.get(q));
        }
        System.out.println(listOfAllRowCombinations.size());
    }

    public HashMap calculateRowCombinations() {
        listOfAllRowCombinations = new HashMap<>();
        listOfProductRows.put("products", 40);
        listOfProductRows.put("category", 9);

        listOfLocationRows.put("coordinates", 3861358);
        listOfLocationRows.put("district", 143);
        listOfLocationRows.put("county", 15);
        listOfLocationRows.put("city", 2);
        listOfLocationRows.put("country", 1);

        listOfOpinionRows.put("Opinion", 3);

        listOfDateRows.put("day", 1116);
        listOfDateRows.put("month", 36);
        listOfDateRows.put("year", 3);
        //combine product and location


        for (String s : listOfProductRows.keySet())
            listOfAllRowCombinations.put(s, listOfProductRows.get(s));
        for (String s : listOfLocationRows.keySet())
            listOfAllRowCombinations.put(s, listOfLocationRows.get(s));
        for (String s : listOfOpinionRows.keySet())
            listOfAllRowCombinations.put(s, listOfOpinionRows.get(s));
        for (String s : listOfDateRows.keySet())
            listOfAllRowCombinations.put(s, listOfDateRows.get(s));

        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfLocationRows.keySet()) {
                listOfAllRowCombinations.put(s + "-" + q, listOfProductRows.get(q) * listOfLocationRows.get(s));
            }
        }
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfOpinionRows.keySet()) {
                listOfAllRowCombinations.put(s + "-" + q, listOfProductRows.get(q) * listOfOpinionRows.get(s));
            }
        }
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfDateRows.keySet()) {
                listOfAllRowCombinations.put(s + "-" + q, listOfProductRows.get(q) * listOfDateRows.get(s));
            }
        }

        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfLocationRows.keySet()) {
                for (String p : listOfOpinionRows.keySet()) {
                    listOfAllRowCombinations.put(s + "-" + q + "-" + p, listOfProductRows.get(q) * listOfLocationRows.get(s) * listOfOpinionRows.get(p));
                }
            }
        }
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfLocationRows.keySet()) {
                for (String p : listOfDateRows.keySet()) {
                    listOfAllRowCombinations.put(s + "-" + q + "-" + p, listOfProductRows.get(q) * listOfLocationRows.get(s) * listOfDateRows.get(p));
                }
            }
        }
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfOpinionRows.keySet()) {
                for (String p : listOfDateRows.keySet()) {
                    listOfAllRowCombinations.put(s + "-" + q + "-" + p, listOfProductRows.get(q) * listOfOpinionRows.get(s) * listOfDateRows.get(p));
                }
            }
        }
        for (String q : listOfProductRows.keySet()) {
            for (String s : listOfOpinionRows.keySet()) {
                for (String p : listOfDateRows.keySet()) {
                    for (String l : listOfLocationRows.keySet()) {
                        listOfAllRowCombinations.put(s + "-" + q + "-" + p + "-" + l, listOfProductRows.get(q) * listOfOpinionRows.get(s) * listOfDateRows.get(p) * listOfLocationRows.get(l));
                    }
                }
            }
        }

        return listOfAllRowCombinations;
    }
}
