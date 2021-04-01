package ru.job4j.dream.store;

import ru.job4j.dream.model.User;

public interface UserStore extends Store<User> {
    User findByEmail(String email);

    void delete(String email);

    boolean checkPass(User user);
}
