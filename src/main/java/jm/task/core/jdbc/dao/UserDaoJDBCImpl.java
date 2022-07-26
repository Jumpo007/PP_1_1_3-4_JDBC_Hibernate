package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final Connection CONNECTION = Util.getConnection();
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS user1";
    private static final String CREATE_TABLE = ("CREATE TABLE IF NOT EXISTS user1" +
            "(id mediumint not null auto_increment," +
            "name VARCHAR(50)," +
            "lastname VARCHAR(50)," +
            "age tinyint," +
            "PRIMARY KEY (id))");
    private static final String ADD_USER = "INSERT INTO user1(name, lastName, age) VALUES (?,?,?)";
    private static final String DELETE_USER = "DELETE FROM user1 WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM user1";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE  user1";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.execute(CREATE_TABLE);
            CONNECTION.commit();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(ADD_USER)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet resultSet = statement.executeQuery(SELECT_ALL);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.execute(CLEAN_TABLE);
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }
}
