package repositories.interfaces;

import java.util.List;

public interface IUserRepository {
    boolean createUser(User user);
    User getUser(int id);
    List<User> getAllUsers();
}
