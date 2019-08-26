package org.database.db2;

import java.sql.*;

public class Db2Connect {

    public static void main(String[] args) {
        Db2Connect db2Connect = new Db2Connect();

        String url = "jdbc:db2://127.0.0.1:50000/myschema";
        String user = "db2inst1";
        String password = "pwd-sample";

        connect(url, user, password);
    }

    private static void connect(String url, String user, String password) {
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");

            System.out.println("**** Loaded the JDBC driver");

            // Create the connection using the IBM Data Server Driver for JDBC and SQLJ
            try (Connection con = DriverManager.getConnection(url, user, password)) {
                // Commit changes manually
                con.setAutoCommit(false);
                System.out.println("**** Created a JDBC connection to the data source");

                showTables(con);

                dropTable(con);
                createTable(con);
                insertDatabase(con, 1, "John", "New York");
                insertDatabase(con, 2, "Bob", "Los Angeles");
                insertDatabase(con, 3, "Karl", "Detroit");
                callDatabase(con, "select customer_id, customer_name, city from customers");


                // Connection must be on a unit-of-work boundary to allow close
                con.commit();
                System.out.println("**** Transaction committed");
            }
            System.out.println("**** Disconnected from data source");

            System.out.println("**** JDBC Exit from class EzJava - no errors");


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void dropTable(Connection conn) {
        try (PreparedStatement preparedStatement = conn.prepareStatement("DROP TABLE customers ")) {
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void showTables(Connection con) throws SQLException {

        System.out.println("**** Table list");
        String sql = "SELECT NAME FROM SYSIBM.SYSTABLES WHERE type = 'T' AND creator <> 'SYSIBM'";
        callDatabase(con, sql);
        System.out.println("**** ****");

    }

    private static void createTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE customers(customer_id SMALLINT NOT NULL, " +
                            "                            customer_name VARCHAR(50) NOT NULL, " +
                            "                            city VARCHAR(50) )");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        try (PreparedStatement preparedStatement = conn.prepareStatement("CREATE TABLE hibern8.customers " +
//                "( customer_id number(10) NOT NULL, " +
//                "  customer_name varchar2(50) NOT NULL, " +
//                "  city varchar2(50) " +
//                ")")) {
//            preparedStatement.executeUpdate();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }


    private static void insertDatabase(Connection conn, int id, String name, String city) {

        final String sql = "INSERT INTO customers (customer_id, customer_name, city) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, city);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void callDatabase(Connection conn, String sql) {
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i < columnCount+1; i++) {
                    System.out.println(metaData.getColumnName(i) + ":" + rs.getString(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
