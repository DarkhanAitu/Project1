package data;

import data.interfaces.IDatabase;
import java.sql.*;

public class PostgresDB implements IDatabase {

    private static PostgresDB instance;
    private Connection connection;

    private PostgresDB() {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try {
            connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PostgresDB getInstance() {
        if (instance == null) {
            instance = new PostgresDB();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
