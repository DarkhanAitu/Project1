package models;

public class Movie {
    private int id;
    private String title;
    private int duration; // мин
    private double price; // цена билета

    public Movie(int id, String title, int duration, double price) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.price = price;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return id + " | " + title + " (" + duration + " min) - $" + price;
    }
}
