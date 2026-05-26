package com.pao.project.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws IOException, SQLException {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IOException("Nu gasesc db.properties in resources");
            }

            properties.load(input);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        loadDriver(url);

        connection = DriverManager.getConnection(url, user, password);

        if (url.contains("sqlite")) {
            try (var statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
        }
    }

    private void loadDriver(String url) throws SQLException {
        try {
            if (url.contains("sqlite")) {
                Class.forName("org.sqlite.JDBC");
            } else if (url.contains("h2")) {
                Class.forName("org.h2.Driver");
            } else if (url.contains("mysql")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Nu gasesc driverul JDBC pentru: " + url, e);
        }
    }

    public static synchronized DatabaseConnection getInstance() throws IOException, SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}