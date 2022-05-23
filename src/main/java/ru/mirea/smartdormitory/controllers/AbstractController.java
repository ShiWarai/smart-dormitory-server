package ru.mirea.smartdormitory.controllers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;
import ru.mirea.smartdormitory.services.AbstractService;

import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T, D extends JpaRepository<T, Long>> {
    protected final AbstractService<T, D> service;

    protected AbstractController(AbstractService<T, D>  service) {
        this.service = service;
    }

    @PostMapping(value = "")
    public T create(@RequestBody T entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    public T update(@PathVariable(name = "id") Long id, @RequestBody T entity) {
        return service.update(id, entity);
    }

    @GetMapping("/{id}")
    public T read(@PathVariable(name = "id") Long id) {
        return service.findById(id);
    }

    @GetMapping("")
    public List<T> readAll() {
        return service.getAll();
    }

    // !!!
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        if (service.findById(id) != Optional.empty()) {
            System.out.println(service.findById(id));
            service.delete(id);
        }

    }
}
