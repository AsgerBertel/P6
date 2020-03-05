package Sql;

import atlas.Atlas;
import atlas.City;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LatLongConverter {



    public static void main(String[] args) throws IOException{
        Atlas atlas = new Atlas();
        City city= atlas.find(40.745118, -74.015273);
        System.out.println(city);

    }
}
