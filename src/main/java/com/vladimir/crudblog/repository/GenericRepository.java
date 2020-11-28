package com.vladimir.crudblog.repository;

import java.util.List;

public interface GenericRepository<T, ID> {

    T save(T t);

    T update(T t);

    T getById(Long id);

    void deleteById(ID id);

    List<T> getAll();
}
