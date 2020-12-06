package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.io.JavaIoPostRepositoryImpl;

import java.util.List;

public class PostController {
    private PostRepository repository = JavaIoPostRepositoryImpl.getInstance();

    public List<Post> getAll() {
        return repository.getAll();
    }

    public Post addPost(String content){
        Post post = new Post(null, content);
        repository.save(post);
        return post;
    }

    public Post getByID(Long id) {
        return repository.getById(id);
    }

    public void deleteByID(Long id){
        repository.deleteById(id);
    }

    public Post update(Post post){
        repository.update(post);
        return post;
    }


}
