package Sql;

import atlas.Atlas;
import java.sql.SQLException;

public class InsertThread extends Thread {
    Atlas atlas;
    SynchronizedLocationManager resultSet;

    public InsertThread(SynchronizedLocationManager resultSet, Atlas atlas) {
        this.resultSet = resultSet;
        this.atlas = atlas;
    }

    @Override
    public void run() {
        insertIntoLocation();
    }

    private void insertIntoLocation() {
        int count=0;
        while(true){
            try{
                DatabaseLocation databaseLocation = resultSet.syncedGetNext(this.atlas);
                ConnectionManager.updateSql(QueryManager.insertIntoLocation(
                        databaseLocation.city.name, databaseLocation.city.admin2, databaseLocation.city.admin1, databaseLocation.city.countryCode,databaseLocation.latitude ,databaseLocation.longitude));
                count++;
                if(count % 100 == 0){
                    System.out.println(this.getName() + "has done " + count);
                }
            }catch (LastRowReachedException | NullPointerException | SQLException e){
                System.out.println(e.getMessage());
                break;
            }
        }
    }
}

