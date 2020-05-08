package Sql;

import java.sql.*;

public class ConnectionManager {
    public static void updateSql(String query) {
        try (Connection conn = DriverManager.getConnection(QueryManager.connectionString)) {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet selectSQL(String query) throws SQLException {
        try (Connection conn = DriverManager.getConnection(QueryManager.connectionString)) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            return rs;
        } catch (SQLException e){
            e.printStackTrace();
        }
        System.exit(420);
        return null;
    }

    public static int updateSqlWithGeneratedKey(String query, String primaryKeyName) {
        String[] id_col = {primaryKeyName};
        try (Connection conn = DriverManager.getConnection(QueryManager.connectionString)) {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query, id_col);
            ResultSet rs = statement.getGeneratedKeys();

            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
