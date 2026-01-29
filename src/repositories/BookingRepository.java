package repositories;

import data.PostgresDB;

import java.sql.*;

public class BookingRepository {

    // Проверка, существует ли место
    public boolean doesSeatExist(int seatId) {
        String sql = "SELECT COUNT(*) FROM seats WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Проверка, занято ли место для конкретного фильма
    public boolean isSeatTaken(int seatId, int movieId) {
        String sql = "SELECT COUNT(*) FROM tickets WHERE seat_id = ? AND movie_id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ps.setInt(2, movieId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Создание бронирования и билета
    public void createBooking(int userId, int movieId, int seatId, double price) {
        try {
            Connection conn = PostgresDB.getInstance().getConnection();

            // Создаём бронирование
            PreparedStatement booking = conn.prepareStatement(
                    "INSERT INTO bookings(user_id) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            booking.setInt(1, userId);
            booking.executeUpdate();

            ResultSet keys = booking.getGeneratedKeys();
            keys.next();
            int bookingId = keys.getInt(1);

            // Создаём билет
            PreparedStatement ticket = conn.prepareStatement(
                    "INSERT INTO tickets(booking_id, seat_id, movie_id, price) VALUES (?,?,?,?)"
            );
            ticket.setInt(1, bookingId);
            ticket.setInt(2, seatId);
            ticket.setInt(3, movieId);
            ticket.setDouble(4, price);
            ticket.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получить цену места по типу
    public double getSeatPrice(int seatId) {
        String sql = "SELECT seat_type FROM seats WHERE id = ?";
        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, seatId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("seat_type");
                switch (type.toLowerCase()) {
                    case "standard": return 150;
                    case "comfort": return 300;
                    case "vip": return 500;
                    default: return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Показать все места с их типами
    public void showSeats() {
        String sql = "SELECT id, seat_number, seat_type FROM seats ORDER BY id";
        try (Statement st = PostgresDB.getInstance().getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            System.out.println("Available seats:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String number = rs.getString("seat_number");
                String type = rs.getString("seat_type");
                System.out.println(id + ": " + number + " (" + type + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Показать полную информацию о бронировании
    public void getFullBooking(int bookingId) {
        String sql = "SELECT b.id AS booking_id, b.booking_date, " +
                "u.username, u.role, " +
                "m.title, m.duration, m.price AS movie_price, " +
                "s.seat_number, s.seat_type, h.name AS hall_name, t.price AS ticket_price " +
                "FROM bookings b " +
                "JOIN users u ON b.user_id = u.id " +
                "JOIN tickets t ON t.booking_id = b.id " +
                "JOIN movies m ON t.movie_id = m.id " +
                "JOIN seats s ON t.seat_id = s.id " +
                "JOIN halls h ON s.hall_id = h.id " +
                "WHERE b.id = ? " +
                "ORDER BY s.seat_number";

        try (PreparedStatement ps = PostgresDB.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            boolean hasResult = false;
            while (rs.next()) {
                hasResult = true;
                System.out.printf(
                        "Booking %d by %s (%s) for movie '%s' [%d min] Seat %s (%s) in hall %s, Ticket price: %.2f, Movie price: %.2f%n",
                        rs.getInt("booking_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("title"),
                        rs.getInt("duration"),
                        rs.getString("seat_number"),
                        rs.getString("seat_type"),
                        rs.getString("hall_name"),
                        rs.getDouble("ticket_price"),
                        rs.getDouble("movie_price")
                );
            }
            if (!hasResult) {
                System.out.println("No bookings found with this Booking ID!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
