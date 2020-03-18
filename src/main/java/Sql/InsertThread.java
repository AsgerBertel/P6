package Sql;

import atlas.Atlas;
import atlas.City;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertThread extends Thread {
    Atlas atlas = new Atlas();
    ResultSet resultSet;
    boolean isEven;

    public InsertThread(boolean isEven) {
        this.isEven = isEven;
    }

    @Override
    public void run() {
        try {
            insertIntoLocation();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIntoLocation() throws SQLException {
        resultSet = ConnectionManager.selectSQL(QueryManager.selectCoordinatesFromTweet);
        while (resultSet.next()) {
            if(isEven){
                if(resultSet.getInt(1) % 2 == 0){
                    City city = this.atlas.find(resultSet.getDouble(1), resultSet.getDouble(2));
                    ConnectionManager.updateSql(QueryManager.insertIntoLocation(city.name, city.admin2, city.admin1, city.countryCode));
                }
            }
            else{
                if (resultSet.getInt(1) % 2 !=0){
                    City city = this.atlas.find(resultSet.getDouble(1), resultSet.getDouble(2));
                    ConnectionManager.updateSql(QueryManager.insertIntoLocation(city.name, city.admin2, city.admin1, city.countryCode));
                }
            }
        }
    }
}
