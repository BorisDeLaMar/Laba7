package ru.itmo.server.src.Comms.database;

import ru.itmo.common.DatabaseAccess;
import ru.itmo.server.src.GivenClasses.Worker;

import java.sql.*;

public class DB_Worker {

    public DB_Worker() throws SQLException {
        String createStatement = "CREATE TABLE IF NOT EXISTS WORKER (" +
                "id SERIAL PRIMARY KEY CHECK(id > 0)," +
                "name TEXT NOT NULL CHECK(name <> '')," +
                "salary BIGINT CHECK(salary > 0)," +
                "position TEXT," +
                "status TEXT NOT NULL," +
                "orgName TEXT," +
                "orgType TEXT," +
                "xCord INTEGER CHECK(xCord < 176)," +
                "yCord FLOAT CHECK(yCord < 729)," +
                "creationDate TEXT," +
                "user_login TEXT NOT NULL REFERENCES USERS(user_login)" +
                ");";
        create(createStatement);
    }
    public void create(String createStatement) throws SQLException {

        Connection connection = DatabaseAccess.getDBConnection();
        PreparedStatement statement = connection.prepareStatement(createStatement);
        statement.execute();
    }
    public static long addWorker(Worker w, String user_login, Connection connection) throws SQLException{
        String sql = "INSERT INTO WORKER (name, salary, position, status, orgName, orgType, xCord, yCord, creationDate, user_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement add = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        add.setString(1, w.getName());
        add.setLong(2, w.getSalary());
        add.setString(3, w.getPosition().getName());
        add.setString(4, w.getStatus().toString());
        add.setString(5, w.getOrganization().getName());
        add.setString(6, w.getOrganization().getType().toString());
        add.setLong(7, w.getCoordinates().getAbscissa());
        add.setDouble(8, w.getCoordinates().getOrdinate());
        add.setString(9, w.getCreationDate().toString());
        add.setString(10, user_login);
        add.execute();
        ResultSet resultSet = add.getGeneratedKeys();
        resultSet.next();
        return resultSet.getLong(1);
    }
    public static void deleteWorker(long id, Connection connection) throws SQLException{
        String sql = "DELETE FROM worker WHERE id = '" + id + "'";
        PreparedStatement delete = connection.prepareStatement(sql);
        delete.execute();
    }
    public static void updateWorker(Worker w, Connection connection) throws SQLException{
        String sql = "UPDATE worker SET name = '" + w.getName() + "', " +
                "salary = '" + w.getSalary() + "', " +
                "position = '" + w.getPosition().toString() + "', " +
                "status = '" + w.getStatus().toString() + "', " +
                "orgName = '" + w.getOrganization().getName() + "', " +
                "orgType = '" + w.getOrganization().getType().toString() + "', " +
                "xCord = '" + w.getCoordinates().getAbscissa() + "', " +
                "yCord = '" + w.getCoordinates().getOrdinate() + "', " +
                "creationDate = '" + w.getCreationDate().toString() + "' WHERE id = '" + w.getId() + "';";
        PreparedStatement update = connection.prepareStatement(sql);
        update.execute();
    }
}
