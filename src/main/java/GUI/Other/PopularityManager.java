package GUI.Other;

import Sql.ConnectionManager;
import Sql.QueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;

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
            } boolean viewExists = false;
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
                    System.out.println("Insert:"+QueryManager.insertIntopopularity(viewName, currentDay));
                } else {
                    ConnectionManager.updateSql(QueryManager.updatepopularityIfExists(viewName));
                    System.out.println("Update:"+QueryManager.updatepopularityIfExists(viewName));

                }
            } else {
                resultSet = ConnectionManager.selectSQL(QueryManager.selectDayFromPopulation);
                int i = 0;
                while (resultSet.next()) {
                    i = resultSet.getInt(1);
                }
                ConnectionManager.updateSql(QueryManager.insertNewDayIntoPopularity(viewName,i +1 ));
            }

        }
    }
}
