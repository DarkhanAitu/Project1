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

    public String getCurrentUserRole() {
        if (currentUser == null) return "";
        return currentUser.getRole();
    }

    public void showMovies() {
        List<Movie> movies = movieRepo.getAll();
        for (Movie m : movies) {
            // Выводим ID, название, длительность и цену
            System.out.println(m.getId() + " | " + m.getTitle() + " (" + m.getDuration() + " min) - $" + m.getPrice());
        }
    }

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

        Movie movie = new Movie(0, title, duration, price); // ID = 0, Postgres сам присвоит SERIAL

        movieRepo.addMovie(movie);

        System.out.println("Movie added successfully!");
    }

    public void bookTicket() {
        System.out.print("Movie ID: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.showSeatsWithStatus(movieId);

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

    public void showFullBooking() {
        System.out.print("Enter Movie ID to view all bookings: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.getFullBooking(currentUser.getId(), movieId);
    }
}

