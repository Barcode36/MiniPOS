package com.alphasoft.pos.contexts;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static Properties connectionProperties;
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {

        try {
            connectionProperties = new Properties();
            connectionProperties.load(ConnectionManager.class.getResourceAsStream("connection.properties"));
            DRIVER = connectionProperties.getProperty("DRIVER");
            URL = connectionProperties.getProperty("URL");
            USER = connectionProperties.getProperty("USER");
            PASSWORD = connectionProperties.getProperty("PASSWORD");
            Class.forName(DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}