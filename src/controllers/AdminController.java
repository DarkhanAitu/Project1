package controllers;

import controllers.interfaces.IAdminController;
import models.Movie;
import models.MovieCategory;
import models.User;
import Factories.MovieFactory;
import repositories.BookingRepository;
import repositories.MovieRepository;
import repositories.UserRepository;

import java.util.Scanner;

public class AdminController implements IAdminController {

    private final Scanner scanner = new Scanner(System.in);
    private final MovieRepository movieRepo = new MovieRepository();
    private final UserRepository userRepo = new UserRepository();
    private final User currentUser;
    private final BookingRepository bookingRepo = new BookingRepository();

    public AdminController(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void addMovie() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            System.out.println("Access denied.");
            return;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Duration (minutes): ");
        int duration = Integer.parseInt(scanner.nextLine());

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.print("Category (ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI): ");
        MovieCategory category;
        try {
            category = MovieCategory.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            category = MovieCategory.OTHER;
        }

        Movie movie = MovieFactory.createMovie(0, title, duration, price, category);
        movieRepo.addMovie(movie);

        System.out.println("Movie added successfully.");
    }

    @Override
    public void addAdmin() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            System.out.println("Access denied.");
            return;
        }

        System.out.print("New admin username: ");
        String username = scanner.nextLine().trim();

        if (userRepo.findByUsername(username) != null) {
            System.out.println("User already exists.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("admin");

        userRepo.addUser(admin);
        System.out.println("New admin added.");
    }
    public void showFullBookingForMovie() {
        System.out.print("Enter Movie ID to view all bookings: ");
        int movieId = Integer.parseInt(scanner.nextLine());

        bookingRepo.getFullBookingByMovie(movieId);
    }

}
