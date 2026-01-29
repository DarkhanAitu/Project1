package repositories;

import data.PostgresDB;
import models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    public List<Movie> getAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Statement st = PostgresDB.getInstance().getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void addMovie(String title, int duration, double price) {
        String sql = "INSERT INTO movies(title, duration, price) VALUES (?, ?, ?)";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setInt(2, duration);
            ps.setDouble(3, price);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getPrice(int movieId) {
        String sql = "SELECT price FROM movies WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
