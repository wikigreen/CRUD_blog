package com.vladimir.crudblog.view;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.RegionRepository;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PostRepository repository = PostRepository.getInstance();
        //for (int i =0; i < 10; i++ ) repository.save(new Post(null, "Hello"));
        repository.getList().stream()
                .filter(p -> p.getId() % 2 == 0)
                .forEach(p -> {
                    repository.deleteById(p.getId());
                });
        repository.getList().stream().map(Post::toString).map(s -> s + "\n").forEach(System.out::println);
    }
}
