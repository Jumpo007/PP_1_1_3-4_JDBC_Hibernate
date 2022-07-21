package jm.task.core.jdbc.util;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static String dbUrl = "jdbc:mysql://localhost:3306/mydbtest";
    private static String dbUserName = "root";

    private static String dbPassWord = "roots";
    public static Connection getConnection() {
        Connection connection;

        try {
            connection = DriverManager.getConnection(dbUrl,dbUserName,dbPassWord);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
