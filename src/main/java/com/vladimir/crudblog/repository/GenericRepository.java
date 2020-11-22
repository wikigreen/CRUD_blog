package com.vladimir.crudblog.repository;

import com.vladimir.crudblog.model.Region;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface GenericRepository<T, ID> {

    public T save(T t);

    public T update(T t);

    public void deleteById(ID id);

    public List<T> getList();
}
