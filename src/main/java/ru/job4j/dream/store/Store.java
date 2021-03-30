package ru.job4j.dream.store;

import java.util.Collection;

public interface Store<T> {
    Collection<T> findAll();

    void save(T item);

    void delete(int id);

    T findById(int id);
}
