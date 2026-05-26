package com.pao.project.elearning.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    public static void init(Connection connection) throws SQLException, IOException {
        try (InputStream input = SchemaInitializer.class
                .getClassLoader()
                .getResourceAsStream("schema.sql")) {

            if (input == null) {
                throw new IOException("Nu gasesc schema.sql in resources");
            }

            String sql = new String(input.readAllBytes());
            String[] statements = sql.split(";");

            try (Statement statement = connection.createStatement()) {
                for (String currentStatement : statements) {
                    String trimmedStatement = currentStatement.trim();

                    if (!trimmedStatement.isEmpty()) {
                        statement.execute(trimmedStatement);
                    }
                }
            }
        }
    }
}