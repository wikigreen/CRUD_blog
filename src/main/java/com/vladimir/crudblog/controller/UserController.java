package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.UserRepository;

import java.util.List;

public class UserController {
    private static UserRepository repository = UserRepository.getInstance();
    private static PostRepository postRepository = PostRepository.getInstance();

    public static List<User> getAll(){
        return repository.getAll();
    }

    public static User addUser(String firstName, String lastName, List<Post> posts, Region region, Role role){
        User user = new User(null, firstName, lastName, posts, region, role);
        repository.save(user);
        return user;
    }

    public static User getByID(Long id) {
        return repository.getById(id);
    }

    public static void deleteByID(Long id) {
        repository.deleteById(id);
    }

    public static User update(Long id, String firstName, String lastName, Region region, Role role, List<Long> toAdd, List<Long> toDelete){
        User user = repository.getById(id);
        if(firstName != null)
            user.setFirstName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(region != null)
            user.setRegion(region);
        if(role != null)
            user.setRole(role);
        for(Long addId: toAdd){
            Post post = postRepository.getById(addId);
            if(post != null && !user.getPosts().contains(post)){
                user.getPosts().add(post);
                System.out.println("Post with ID " + addId + " has been added to user`s posts list successfully");
            } else {
                System.out.println("Post with ID " + addId + " does not exists" );
            }

        }
        for(Long deleteId: toDelete){
            Post post = postRepository.getById(deleteId);
            if(post != null){
                if(user.getPosts().remove(post))
                    System.out.println("Post with ID " + deleteId + " has been deleted from user`s post list successfully");
                else
                    System.out.println("Post with ID " + deleteId + " was not in user`s post list");
            } else {
                System.out.println("Post with ID " + deleteId + " does not exists" );
            }
        }
        return repository.update(user);
    }

    public static boolean exists(Long id){
        return repository.getById(id) != null;
    }

}
