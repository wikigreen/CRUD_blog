package com.vladimir.crudblog.view;

import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.RegionRepository;
import com.vladimir.crudblog.repository.UserRepository;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //clearReps();
        fillRep();
        UserRepository userRepository = UserRepository.getInstance();
        userRepository.getList().stream().forEach(System.out::println);
        System.out.println();

        //userRepository.getList().stream().forEach(System.out::println);
        //userRepository.getList().stream().flatMap(u -> u.getPosts().stream()).forEach(System.out::println);
        //changeReps();
        //userRepository.getList().stream().flatMap(u -> u.getPosts().stream()).forEach(System.out::println);
    }

   static void fillRep() {
        RegionRepository regionRepository = RegionRepository.getInstance();
        PostRepository postRepository = PostRepository.getInstance();
        UserRepository userRepository = UserRepository.getInstance();

        Region myRegion = new Region(null, "USA");
        regionRepository.save(myRegion);

        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Post tempPost = new Post(null, "Post №" + (i + 11));
            postRepository.save(tempPost);
            posts.add(tempPost);
        }
        userRepository.save(new User(null, "Dima", "Pupkin", posts, myRegion, Role.ADMIN));
   }

   static void clearReps(){
       try {
           new PrintWriter("src//main//resources//files//posts.txt").close();
           new PrintWriter("src//main//resources//files//regions.txt").close();
           new PrintWriter("src//main//resources//files//users.txt").close();
       } catch (IOException e) {
           System.err.println("Repository has not been cleared");
       }
   }

   static void changeReps(){
       UserRepository.getInstance().getList().stream()
               .flatMap(u -> u.getPosts().stream())
               .forEach(p -> {
                   if(p.getId() % 2 == 0){
                       p.setContent("Changed Post № " + p.getId());
                       PostRepository.getInstance().update(p);
                   }
               });
   }

}