package factories;

import models.User;

public class UserFactory {
    public static User createUser(String input, String username) {
        return switch(input.toLowerCase()) {
            case "admin", "1" -> new User(1, username, "admin");
            case "customer", "2" -> new User(2, username, "customer");
            default -> throw new IllegalArgumentException("Unknown role: " + input);
        };
    }
}

