package ru.itmo.server.src.Comms.database;

import ru.itmo.common.DatabaseAccess;
import ru.itmo.common.authorization.User;

import java.sql.*;

public class DB_User {

    public DB_User() throws SQLException{
        String createStatement = "CREATE TABLE IF NOT EXISTS USERS (" +
                                    "user_login TEXT PRIMARY KEY," +
                                    "user_password TEXT NOT NULL" +
                                    ");";
        create(createStatement);
    }
    public void create(String createStatement) throws SQLException {

        Connection connection = DatabaseAccess.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(createStatement);
        statement.execute();
    }
    public static void addUser(User user, Connection connection) throws SQLException{
        String sql = "INSERT INTO USERS (user_login, user_password) VALUES (?, ?)";
        PreparedStatement add = connection.prepareStatement(sql);
        add.setString(1, user.getLogin());
        add.setString(2, user.getPassword());
        add.execute();
    }
    public static boolean isInBD(String user_login, Connection connection) throws SQLException {
        String sql = "SELECT user_login from USERS";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        int i = 1;
        //System.out.println(user_login + " DB_User line 36");
        while(resultSet.next()){
            if(resultSet.getString(i).equals(user_login)){
                //System.out.println(resultSet.getString(i));
                return true;
            }
        }
        return false;
    }
    public static String getUserPassword(String user_login, Connection connection) throws SQLException{
        String sql = "SELECT user_password from users WHERE user_login = '" + user_login + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getString(1);
    }
}
