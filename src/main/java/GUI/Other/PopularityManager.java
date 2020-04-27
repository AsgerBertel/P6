package GUI.Other;

import Sql.ConnectionManager;
import Sql.QueryManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PopularityManager {
    ResultSet resultSet;


    public void updatePopularityValue(String viewName) throws SQLException {
        resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopulation);
        if (!resultSet.next()) {
            ConnectionManager.updateSql(QueryManager.insertpopularityIfNoRowsExists(viewName));
        } else {
            resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopulation);
            int currentDay = 1;
            boolean isCurrentDayTrue = true;
            while (resultSet.next()) {
                currentDay = resultSet.getInt(3);
                isCurrentDayTrue = resultSet.getBoolean(4);
            }
            if (isCurrentDayTrue) {
                resultSet = ConnectionManager.selectSQL(QueryManager.selectAllFromPopulationWhereCurrentDay);
                boolean viewExists = false;
                while (resultSet.next()) {
                    if (viewName.equals(resultSet.getString(1))) {
                        viewExists = true;
                        break;
                    }
                    if (viewExists == false) {
                        ConnectionManager.updateSql(QueryManager.insertIntopopularity(viewName, currentDay));
                    } else {
                        ConnectionManager.updateSql(QueryManager.updatepopularityIfExists(viewName));

                    }
                }

            } else {
                ConnectionManager.updateSql(QueryManager.insertpopularityIfNoRowsExists(viewName));
            }

        }
    }
}
