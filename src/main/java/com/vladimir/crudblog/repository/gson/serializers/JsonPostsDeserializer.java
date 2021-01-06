package com.vladimir.crudblog.repository.gson.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.repository.PostRepository;
import com.vladimir.crudblog.repository.gson.JsonPostRepositoryImpl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonPostsDeserializer implements JsonDeserializer<List<Post>> {
    @Override
    public List<Post> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        PostRepository postRepository = JsonPostRepositoryImpl.getInstance();
        List<Post> userPosts = new ArrayList<>();

        String serializedList = jsonElement.getAsString();
        if(serializedList.equals(""))
            return userPosts;

        Arrays.stream(serializedList.split(" "))
                .map(Long::parseLong)
                .map(postRepository::getById)
                .filter(post -> post != null)
                .forEach(userPosts::add);

        return userPosts;
    }
}
