package ru.itmo.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccess {
    public static Connection getDBConnection() throws SQLException {
        final String DB_USERNAME = "postgres";
        final String DB_PASSWORD = "6291604";
        final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }
}
