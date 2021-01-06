package com.vladimir.crudblog.repository.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.gson.serializers.JsonDateDeserializer;
import com.vladimir.crudblog.repository.gson.serializers.JsonDateSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonPostRepositoryImpl implements PostRepository {
    private final String POST_JSON_FILE_PATH = "src//main//resources//json//posts.json";
    private static JsonPostRepositoryImpl instance = new JsonPostRepositoryImpl();
    private final Gson gson;
    private final File file;
    private Long lastId;

    public static JsonPostRepositoryImpl getInstance(){
        return instance;
    }

    private JsonPostRepositoryImpl(){
        file = new File(POST_JSON_FILE_PATH);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Date.class, new JsonDateSerializer())
                .registerTypeAdapter(Date.class, new JsonDateDeserializer())
                .create();
    }

    private List<Post> listOfPosts() {
        try {
            JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
            List<Post> postList = gson.fromJson(jsonReader, new TypeToken<List<Post>>(){}.getType());
            if(postList == null)
                return new ArrayList<Post>();
            return postList;
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading from posts.json");
        }
        throw new Error("Can not read from posts.json");
    }

    private void writeList(List<Post> postList){
        try {
            JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(file)));
            jsonWriter.setIndent("  ");
            gson.toJson(postList, new TypeToken<List<Post>>(){}.getType(), jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            System.out.println("File " + file + " is not found");
        }
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null)
            post.setId(generateId());
        List<Post> posts = listOfPosts();
        posts.add(post);
        writeList(posts);
        return post;
    }

    @Override
    public Post update(Post post) {
        List<Post> postList = listOfPosts();

        Post postToUpd = postList.stream()
                .filter(p -> p.getId().equals(post.getId()))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no post with id " + post.getId() + " in repository"));

        postToUpd.setContent(post.getContent());
        postToUpd.setUpdated(new Date());

        writeList(postList);

        return post;
    }

    @Override
    public Post getById(Long id) {
        return listOfPosts().stream()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        List<Post> postList = listOfPosts();
        if(!postList.removeIf(post -> post.getId().equals(id))) {
            throw new IllegalArgumentException("There is no post with id " + id + " in repository");
        }
        writeList(postList);
    }

    @Override
    public List<Post> getAll() {
        return listOfPosts();
    }

    private Long generateId() {
        if(this.lastId == null){
            lastId = listOfPosts()
                    .stream()
                    .map(Post::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }
}
