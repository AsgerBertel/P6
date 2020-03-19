package Sql;

import atlas.City;

public class DatabaseLocation {
    City city;
    double latitude, longitude;

    public DatabaseLocation(City city, double latitude, double longitude) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
