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


        if (url == null || user == null || password == null) {
            url = "jdbc:postgresql://localhost:5432/cinemadb";
            user = "postgres";
            password = "0000";
        }


        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database!");
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
