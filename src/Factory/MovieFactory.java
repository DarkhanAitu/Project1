package factories;

import models.Movie;
import models.MovieCategory;

public class MovieFactory {

    public static Movie createMovie(
            int id,
            String title,
            int duration,
            double price,
            MovieCategory category) {
        category = MovieCategory.OTHER;

        if (title.toLowerCase().contains("star") ||
                title.toLowerCase().contains("interstellar")) {
            category = MovieCategory.SCI_FI;
        }

        return new Movie(id, title, duration, price);
    }
}
