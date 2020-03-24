package Sql;

import atlas.Atlas;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SynchronizedLocationManager {
    final Object lock = new Object();
    ResultSet resultSet;

    public SynchronizedLocationManager(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public DatabaseLocation syncedGetNext(Atlas atlas) throws SQLException {
        synchronized (lock) {
            if (resultSet.next()) {
                double latitude = this.resultSet.getDouble(1);
                double longitude = this.resultSet.getDouble(2);
                return new DatabaseLocation(atlas.find(latitude, longitude), latitude, longitude);
            } else {
                throw new LastRowReachedException("Last row of results set has been reached");
            }
        }
    }
}
