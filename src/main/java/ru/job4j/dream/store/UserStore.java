package ru.job4j.dream.store;

public interface UserStore<T> extends Store<T> {
    T findByEmail(String email);

    void delete(String email);

    boolean checkPass(T user);
}
