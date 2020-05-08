package GUI.Other;

import PerformanceTester.QueriedView;
import Sql.ConnectionManager;
import Sql.QueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PopularityManager {
    ResultSet resultSet;

    public void updatePopularityValue(String viewName) throws SQLException {
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopularity);
        if (!resultSet.next()) {
            ConnectionManager.updateSql(QueryManager.insertpopularityIfNoRowsExists(viewName));
        } else {
            resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopularity);
            int currentDay = 1;
            boolean isCurrentDayTrue = true;
            while (resultSet.next()) {
                currentDay = resultSet.getInt(3);
                isCurrentDayTrue = resultSet.getBoolean(4);
            }
            boolean viewExists = false;
            if (isCurrentDayTrue) {
                resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopulationWhereCurrentDay);

                while (resultSet.next()) {
                    String dbViewName = resultSet.getString(1);
                    if (viewName.equals(dbViewName)) {
                        viewExists = true;
                        break;
                    } else {
                        viewExists = false;

                    }

                }
                if (!viewExists) {
                    ConnectionManager.updateSql(QueryManager.insertIntopopularity(viewName, currentDay));
                    System.out.println("Insert:" + QueryManager.insertIntopopularity(viewName, currentDay));
                } else {
                    ConnectionManager.updateSql(QueryManager.updatepopularityIfExists(viewName));
                    System.out.println("Update:" + QueryManager.updatepopularityIfExists(viewName));

                }
            } else {
                resultSet = ConnectionManager.selectSQL(QueryManager.selectDayFromPopulation);
                int i = 0;
                while (resultSet.next()) {
                    i = resultSet.getInt(1);
                }
                ConnectionManager.updateSql(QueryManager.insertNewDayIntoPopularity(viewName, i + 1));
            }

        }
    }

    public void updatePopularityValues(Set<QueriedView> queriedViews) throws SQLException {
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopularity);
        ArrayList<PopularityTableEntry> existingRows = getExistingRows(resultSet);
        for (QueriedView qv : queriedViews) {
            if (existingRows.isEmpty()) {
                //If pop table is empty just add our shit with day as 1.
                ConnectionManager.updateSql(QueryManager.insertPopularityIfNoRowsExistWithValue(qv.getViewName(), qv.getAmount_of_queries(), 1));
            } else {
                boolean isUpdated = false;
                //else we need to find the correct row to update
                for (PopularityTableEntry pte : existingRows) {
                    if (qv.getViewName().equals(pte.getViewname()))
                    if (qv.getViewName().equals(pte.getViewname()) && pte.isCurrentDay()) {
                        //if the view is the current day and has the same name as us, we need to update its value
                        ConnectionManager.updateSql(QueryManager.updatePopularityIfExistsWithValue(
                                qv.getViewName(), qv.getAmount_of_queries() + pte.getDailyvalue()
                        ));
                        isUpdated = true;
                        break;
                    }
                }
                if (isUpdated) continue;
                //if view exists but wasn't updated, we need to add a new row with an updated day.
                ConnectionManager.updateSql(QueryManager.insertPopularityIfNoRowsExistWithValue(
                        qv.getViewName(), qv.getAmount_of_queries(), getCurrentDay(existingRows)));

            }
        }
    }

    private ArrayList<PopularityTableEntry> getExistingRows(ResultSet resultSet) throws SQLException {
        ArrayList<PopularityTableEntry> entries = new ArrayList<>();
        while (resultSet.next()) {
            entries.add(new PopularityTableEntry(
                    resultSet.getString(1),
                    resultSet.getInt(2),
                    resultSet.getInt(3),
                    resultSet.getBoolean(4),
                    resultSet.getInt(5)
            ));
        }
        return entries;
    }

    private int getCurrentDay(ArrayList<PopularityTableEntry> rows) {
        int currentDay = -1;
        int highestDay = -1;
        boolean hasCurrentDay=false;
        for (PopularityTableEntry pte : rows) {
            if(pte.getDay() > highestDay){
                highestDay = pte.getDay();
            }
            if (pte.getDay() > currentDay && pte.isCurrentDay()) {
                currentDay = pte.getDay();
                hasCurrentDay=true;
            }
        }
        if(hasCurrentDay )
            return currentDay;
        return highestDay+1;
    }
}
