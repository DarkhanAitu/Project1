package controllers;

import models.Movie;
import models.User;
import repositories.BookingRepository;
import repositories.MovieRepository;

import java.util.List;
import java.util.Scanner;

public class BookingController {

    private final BookingRepository bookingRepo = new BookingRepository();
    private final MovieRepository movieRepo = new MovieRepository();
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    // Логин пользователя
    public void login() {
        System.out.print("Are you an admin or a customer? (admin/customer): ");
        String role = scanner.nextLine().trim().toLowerCase();

        if (role.equals("admin")) {
            currentUser = new User(1, "admin_user", "admin");
        } else {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            currentUser = new User(2, name, "customer");
        }

        System.out.println("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
    }

    // Получить роль текущего пользователя
    public String getCurrentUserRole() {
        if (currentUser == null) return "";
        return currentUser.getRole();
    }

    // Показать все фильмы
    public void showMovies() {
        List<Movie> movies = movieRepo.getAll();
        for (Movie m : movies) System.out.println(m);
    }

    // Добавить фильм (только для админа)
    public void addMovie() {
        if (!currentUser.getRole().equals("admin")) {
            System.out.println("You are not allowed to add movies.");
            return;
        }
        System.out.print("Movie title: ");
        String title = scanner.nextLine();
        System.out.print("Duration (min): ");
        int duration = Integer.parseInt(scanner.nextLine());
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        movieRepo.addMovie(title, duration, price);
        System.out.println("Movie added successfully!");
    }

    // Бронирование билета
    public void bookTicket() {
        System.out.print("Movie ID: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.showSeats(); // показываем все места

        System.out.print("Enter Seat ID: ");
        int seatId = Integer.parseInt(scanner.nextLine());

        if (!bookingRepo.doesSeatExist(seatId)) {
            System.out.println("Seat does not exist!");
            return;
        }
        if (bookingRepo.isSeatTaken(seatId, movieId)) {
            System.out.println("Seat already booked!");
            return;
        }

        double price = bookingRepo.getSeatPrice(seatId);

        bookingRepo.createBooking(currentUser.getId(), movieId, seatId, price);
        System.out.println("Ticket booked successfully! Price: " + price);
    }

    // Показать полную информацию о бронировании
    public void showFullBooking() {
        System.out.print("Enter Booking ID: ");
        int bookingId = Integer.parseInt(scanner.nextLine());
        bookingRepo.getFullBooking(bookingId);
    }
}
