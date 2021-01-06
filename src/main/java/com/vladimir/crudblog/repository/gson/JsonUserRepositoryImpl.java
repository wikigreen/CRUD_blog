package com.vladimir.crudblog.repository.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.UserRepository;
import com.vladimir.crudblog.repository.gson.serializers.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUserRepositoryImpl implements UserRepository {
    private final String USER_JSON_FILE_PATH = "src//main//resources//json//users.json";
    private static JsonUserRepositoryImpl instance = new JsonUserRepositoryImpl();
    private final Gson gson;
    private final File file;
    private Long lastId;

    public static JsonUserRepositoryImpl getInstance(){
        return instance;
    }

    private JsonUserRepositoryImpl(){
        file = new File(USER_JSON_FILE_PATH);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(new TypeToken<List<Post>>(){}.getType(), new JsonPostsSerializer())
                .registerTypeAdapter(new TypeToken<List<Post>>(){}.getType(), new JsonPostsDeserializer())
                .registerTypeAdapter(Region.class, new JsonRegionSerializer())
                .registerTypeAdapter(Region.class, new JsonRegionDeserializer())
                .create();
    }

    private List<User> listOfUsers() {
        try {
            JsonReader jsonReader =  new JsonReader(new BufferedReader(new FileReader(file)));
            List<User> userList = gson.fromJson(jsonReader, new TypeToken<List<User>>(){}.getType());
            if(userList == null)
                return new ArrayList<>();
            return userList;
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading from users.json");
        }
        throw new Error("Can not read from users.json");
    }

    private void writeList(List<User> userList){
        try {
            JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(file)));
            jsonWriter.setIndent("  ");
            gson.toJson(userList, new TypeToken<List<User>>(){}.getType(), jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            System.out.println("File " + file + " is not found");
        }
    }

    @Override
    public User save(User user) {
        if (user.getId() == null)
            user.setId(generateId());
        List<User> users = listOfUsers();
        users.add(user);
        writeList(users);
        return user;
    }

    @Override
    public User update(User user) {
        List<User> userList = listOfUsers();

        User userToUpd = userList.stream()
                .filter(p -> p.getId().equals(user.getId()))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no post with id " + user.getId() + " in repository"));

        userToUpd.setFirstName(user.getFirstName());
        userToUpd.setLastName(user.getLastName());
        userToUpd.setPosts(user.getPosts());
        userToUpd.setRegion(user.getRegion());
        userToUpd.setRole(user.getRole());

        writeList(userList);

        return user;
    }

    @Override
    public User getById(Long id) {
        return listOfUsers().stream()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        List<User> userList = listOfUsers();
        if(!userList.removeIf(user -> user.getId().equals(id))) {
            throw new IllegalArgumentException("There is no user with id " + id + " in repository");
        }
        writeList(userList);
    }

    @Override
    public List<User> getAll() {
        return listOfUsers();
    }

    private Long generateId() {
        if(this.lastId == null) {
            lastId = listOfUsers()
                    .stream()
                    .map(User::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }
}
