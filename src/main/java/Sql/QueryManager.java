package Sql;


import java.sql.ResultSet;

public class QueryManager {
    public static String connectionString = "jdbc:postgresql://62.107.118.55:5432/cubefrequency?user=postgres&password=password";
    public static String selectDateFromTweet = "SELECT date FROM cube.tweets ";
    public static String selectCoordinatesFromTweet = "SELECT lat,long FROM cube.tweets WHERE cube.tweets.tweetid > 10000000";
    public static String selectAllFromTweet = "SELECT product,lat,long,opinion,date FROM cube.tweets Where tweetid > 162928 AND tweetid < 2000000";
    public static String selectProductIDFromProduct = "SELECT lat,long FROM cube.tweets ";
    public static String selectCoordinatesidFromCoordinates = "SELECT locationid FROM cubefrequency.cube.coordinate";
    public static String dropSchemaPublic = "DROP SCHEMA IF EXISTS public CASCADE; CREATE SCHEMA public;";
    public static String selectAllFromPopularity = "SELECT * FROM cube.popularity";
    public static String selectAllFromPopulationWhereCurrentDay = "SELECT * FROM cube.popularity WHERE currentday = true";
    public static String selectDayFromPopulation = "SELECT day FROM cube.popularity";
    public static String updateCurrentDayInPopularity = "UPDATE cube.popularity set currentday = false";
    public static String selectAllFromViewsize = "SELECT viewname, size FROM cubefrequency.cube.viewsize";
    public static String selectAllVirtualViewNames = "SELECT viewname from cubefrequency.pg_catalog.pg_views WHERE schemaname = 'public'";
    public static String selectAllMaterializedlViewNames = "SELECT matviewname from cubefrequency.pg_catalog.pg_matviews WHERE schemaname = 'public'";
    public static String deleteContentsTablePopularity = "DELETE FROM cubefrequency.cube.popularity";
    public static String selectAllVirtualViewNamesAndDefinitions = "SELECT viewname, definition from cubefrequency.pg_catalog.pg_views WHERE schemaname = 'public'";
    public static String selectAllMaterializedlViewNamesAndDefinitions = "SELECT matviewname, definition from cubefrequency.pg_catalog.pg_matviews WHERE schemaname = 'public'";
    public static String getAccumulatedDailyValue = "SELECT SUM(dailyvalue) FROM cube.popularity WHERE currentday = 'true'";
    public static String getAccumulatedGlobalValue = "SELECT SUM(dailyvalue) FROM cube.popularity";

    public static String dropMaterializedView(String viewname){
        return "drop materialized view cubefrequency.public." + viewname + " cascade";
    }

    public static String dropVirtualView(String viewname) {
        return "drop view cubefrequency.public." + viewname;
    }

    public static String insertIntoViewsize(String viewName, long size) {
        return "INSERT INTO cubefrequency.cube.viewsize(viewname, size) VALUES ('" + viewName + "', " + size + ")";
    }

    public static String insertIntoDate(int day, int month, int year) {
        return "INSERT INTO cubefrequency.cube.datedimension (year,month,day)  VALUES ('" + year + "', '" + month + "', '" + day + "')";
    }

    public static String selectTopTopics(String searchWord) {
        return "SELECT toptopic.toptopic FROM cube.toptopic WHERE toptopic.toptopic LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectDistrict(String searchWord) {
        return "SELECT district.district FROM cube.district WHERE district.district LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectCounty(String searchWord) {
        return "SELECT county.county  FROM cube.county WHERE county.county LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectCity(String searchWord) {
        return "SELECT city.city  FROM cube.city WHERE city.city LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectCountry(String searchWord) {
        return "SELECT country.country FROM cube.country WHERE country.country LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectDay(String searchWord) {
        return "SELECT day.day  FROM cube.day WHERE CAST(day.day AS TEXT)LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectMonth(String searchWord) {
        return "SELECT month.month FROM cube.month WHERE CAST(month.month AS TEXT)LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectYear(String searchWord) {
        return "SELECT year.year FROM cube.year WHERE CAST(year.year AS TEXT) LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectOpinions(String searchWord) {
        return "SELECT opinion.opinion FROM cube.opinion WHERE opinion.opinion  LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectSubTopics(String searchWord) {
        return "SELECT Distinct subtopic.subtopic FROM cube.subtopic WHERE subtopic.subtopic LIKE '%" + searchWord + "%' limit 5";
    }

    public static String selectTopTopicFromView(String topic, String view) {
        return "SELECT * FROM cubefrequency.public." + view + " WHERE toptopic = '" + topic + "'";
    }

    public static String selectSubTopicFromView(String topic, String view) {
        return "SELECT * FROM cubefrequency.public." + view + " WHERE subtopic = '" + topic + "'";
    }

    public static String selectView(String view) {
        return "SELECT * FROM cubefrequency.public." + view + "";
    }

    public static String insertIntoYear(int year) {
        return "INSERT INTO cubefrequency.cube.year (year) VALUES (" + year + ")";
    }

    public static String insertIntoMonth(int month, int year) {
        return "INSERT INTO cubefrequency.cube.month (month, yearid) VALUES(" + month + ", " + year + ")";
    }

    public static String insertIntoDay(int day, int month) {
        return "INSERT INTO cube.day (monthid,day)  VALUES (" + month + ", " + day + ")";
    }

    public static String selectProductIDFromProduct(String product) {
        return "SELECT productid FROM cube.product WHERE product.product = '" + product + "'";
    }

    public static String selectOpinionIDFromOpinion(String opinion) {
        return "SELECT opinionid FROM cube.opinion WHERE opinion.opinion = '" + opinion + "'";
    }

    public static String selectLocationIDFromCoordinates(double lat, double longi) {
        return "SELECT coordinate.locationid FROM cube.coordinate WHERE lat = " + lat + " AND long =" + longi + "";
    }

    public static String selectDayIDFromDay(int day, int month, int year) {
        return "SELECT dayid FROM cube.day d INNER JOIN cube.month ON d.monthid = month.monthid INNER JOIN cube.year ON month.yearid = year.yearid WHERE day = " + day + " AND month.month = " + month + " AND year.year =" + year + "";
    }

    /**/
    public static String selectAllProductIDFromProduct() {
        return "SELECT productid FROM cube.product";
    }

    public static String selectAllOpinionIDFromOpinion() {
        return "SELECT opinionid FROM cube.opinion";
    }

    public static String selectAllFromSubTopics() {
        return "SELECT subtopicID, subtopic.subtopic, toptopicid FROM cube.subtopic";
    }

    public static String selecAlltLocationIDFromCoordinates() {
        return "SELECT coordinate.locationid FROM cube.coordinate";
    }

    public static String selectAllDayIDFromDay(int day, int month, int year) {
        return "SELECT dayid FROM cube.day d INNER JOIN cube.month ON d.monthid = month.monthid INNER JOIN cube.year ON month.yearid = year.yearid WHERE day = " + day + " AND month.month = " + month + " AND year.year= " + year + "";
    }

    public static String insertFactTable(int productid, int opinionid, int dateid, int coordinateid) {
        return "INSERT INTO cube.facttable ( productid, opinionid, dateid, coordinateid)\n" +
                "VALUES( " + productid + ", " + opinionid + "," + dateid + "," + coordinateid + ")";
    }

    public static String insertIntoLocation(String district, String county, String City, String country, double lat, double longtitude) {
        return "INSERT INTO cube.locationdimension (district,county,City,country,lat,long)  VALUES ( '" + district + "', '" + county + "','" + City + "','" + country + "','" + lat + "','" + longtitude + "')";
    }

    public static String insertIntoProduct(String product, String category) {
        return "INSERT INTO cube.productdimension (category,product)  VALUES ( '" + product + "','" + category + "')";
    }

    public static String insertIntoTopTopics(int id, String topic) {
        return "INSERT INTO cube.toptopics (toptopicid, topic) VALUES (" + id + "," + "'" + topic + "')";
    }

    public static String insertIntoSubTopics(String topic, int toptopicid) {
        return "INSERT INTO cube.subtopics (subtopic, toptopicid) VALUES ('" + topic + "'," + toptopicid + ")";
    }

    public static String insertpopularityIfNoRowsExists(String view) {
        return "INSERT INTO cube.popularity VALUES ('" + view + "',1,1,true)";
    }

    public static String insertNewDayIntoPopularity(String view, int day) {
        return "INSERT INTO cube.popularity VALUES ('" + view + "',1," + day + ",true)";
    }

    public static String insertIntopopularity(String view, int currentDay) {
        return "INSERT INTO cube.popularity (viewname, dailyvalue, day, currentday) VALUES ('" + view + "',1," + currentDay + ",true)";
    }

    public static String updatepopularityIfExists(String view) {
        return "UPDATE cube.popularity SET dailyvalue = dailyvalue+1 WHERE currentday = true AND viewname = '" + view + "'";
    }
}
