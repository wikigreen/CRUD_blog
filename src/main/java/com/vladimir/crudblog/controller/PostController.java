package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.repository.PostRepository;

import java.util.List;

public class PostController {
    private static PostRepository repository = PostRepository.getInstance();

    public static List<Post> getAll() {
        return repository.getAll();
    }

    public static Post addPost(String content){
        Post post = new Post(null, content);
        repository.save(post);
        return post;
    }

    public static Post getByID(Long id) {
        return repository.getById(id);
    }

    public static void deleteByID(Long id){
        repository.deleteById(id);
    }

    public static Post update(Post post){
        repository.update(post);
        return post;
    }


}
