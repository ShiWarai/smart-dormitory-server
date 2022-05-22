package ru.mirea.smart_dormitory.services;

import java.util.List;

public interface TableService<T> {
    void createEntity(T t);

    List<T> readAllEntity();

    boolean deleteAllEntity();

}
