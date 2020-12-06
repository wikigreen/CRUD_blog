package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.UserRepository;
import com.vladimir.crudblog.repository.io.JavaIOUserRepositoryImpl;

import java.util.List;

public class UserController {
    private static UserRepository userRepository = JavaIOUserRepositoryImpl.getInstance();
    private PostController postController = new PostController();

    public static List<User> getAll(){
        return userRepository.getAll();
    }

    public static User addUser(String firstName, String lastName, List<Post> posts, Region region, Role role){
        User user = new User(null, firstName, lastName, posts, region, role);
        userRepository.save(user);
        return user;
    }

    public static User getByID(Long id) {
        return userRepository.getById(id);
    }

    public static void deleteByID(Long id) {
        userRepository.deleteById(id);
    }

    public User update(Long id, String firstName, String lastName, Region region, Role role, List<Long> toAdd, List<Long> toDelete){
        User user = userRepository.getById(id);
        if(firstName != null)
            user.setFirstName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(region != null)
            user.setRegion(region);
        if(role != null)
            user.setRole(role);
        for(Long addId: toAdd){
            Post post = postController.getByID(addId);
            if(post != null && !user.getPosts().contains(post)){
                user.getPosts().add(post);
                System.out.println("Post with ID " + addId + " has been added to user`s posts list successfully");
            } else {
                System.out.println("Post with ID " + addId + " does not exists" );
            }

        }
        for(Long deleteId: toDelete){
            Post post = postController.getByID(deleteId);
            if(post != null){
                if(user.getPosts().remove(post))
                    System.out.println("Post with ID " + deleteId + " has been deleted from user`s post list successfully");
                else
                    System.out.println("Post with ID " + deleteId + " was not in user`s post list");
            } else {
                System.out.println("Post with ID " + deleteId + " does not exists" );
            }
        }
        return userRepository.update(user);
    }

    public static boolean exists(Long id){
        return userRepository.getById(id) != null;
    }

}
