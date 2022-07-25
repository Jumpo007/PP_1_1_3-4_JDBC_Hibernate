package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection CONNECTION = Util.getConnection();
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
            CONNECTION.setAutoCommit(false);
            statement.execute(CREATE_TABLE);
            CONNECTION.commit();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                CONNECTION.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void dropUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
            CONNECTION.setAutoCommit(false);
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                CONNECTION.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(ADD_USER)) {
            CONNECTION.setAutoCommit(false);
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
        } finally {
            try {
                CONNECTION.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = CONNECTION.prepareStatement(DELETE_USER)) {
            CONNECTION.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                CONNECTION.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement statement = CONNECTION.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_ALL);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");
                users.add(new User(name, lastName, age));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = CONNECTION.createStatement()) {
            CONNECTION.setAutoCommit(false);
            statement.execute(CLEAN_TABLE);

        } catch (SQLException e) {
            try {
                CONNECTION.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        } finally {
            try {
                CONNECTION.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
