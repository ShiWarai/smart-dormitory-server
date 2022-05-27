package ru.mirea.smartdormitory.controllers;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mirea.smartdormitory.services.AbstractService;

public abstract class AbstractController<T, D extends JpaRepository<T, Long>> {
    protected final AbstractService<T, D> service;

    protected AbstractController(AbstractService<T, D>  service) {
        this.service = service;
    }

}
