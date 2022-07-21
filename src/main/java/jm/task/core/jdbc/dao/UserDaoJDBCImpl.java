package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String create_Table =("CREATE TABLE IF NOT EXISTS user1" +
                "(id mediumint not null auto_increment," +
                "name VARCHAR(50)," +
                "lastname VARCHAR(50)," +
                "age tinyint," +
                "PRIMARY KEY (id))");
        try (Statement statement = connection.createStatement()) {
            statement.execute(create_Table);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void dropUsersTable() {
        String DROP_TABLE = "DROP TABLE IF EXISTS user1";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String ADD_USER = "INSERT INTO user1(name, lastName, age) VALUES (?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(ADD_USER)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String DELETE_USER = "DELETE FROM user1 WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        String SELECT_ALL = "SELECT * FROM user1";
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
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
        String CLEAN_TABLE = "TRUNCATE TABLE  user1";
        try (Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_TABLE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
