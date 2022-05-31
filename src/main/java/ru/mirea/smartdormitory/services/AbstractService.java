package ru.mirea.smartdormitory.services;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public  abstract class AbstractService<T, D extends JpaRepository<T, Long>> {
    protected final D repository;

    protected AbstractService(D repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T findById(Long id) {
        Optional<T> obj = repository.findById(id);
        if(!obj.isEmpty())
            return obj.get();
        else
            return null;
    }

    public T update(Long id, T entity) {
        if(findById(id) != null)
            return repository.save(entity);
        else
            return null;
    }

    public boolean delete(Long id) {
        if (findById(id) == null) return false;
        repository.deleteById(id);
        return true;
    }

    public List<T> getAll() {
        Sort sort = Sort.by("id").ascending();
        return repository.findAll(sort);
    }
}
