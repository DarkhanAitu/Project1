package models;

public class Ticket {
    private int id;
    private String movie;
    private int seat;
    private double price;
    private boolean booked;

    public Ticket(int id, String movie, int seat, double price, boolean booked) {
        this.id = id;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
        this.booked = booked;
    }

    public int getId() { return id; }
    public String getMovie() { return movie; }
    public int getSeat() { return seat; }
    public double getPrice() { return price; }
    public boolean isBooked() { return booked; }
}
}
