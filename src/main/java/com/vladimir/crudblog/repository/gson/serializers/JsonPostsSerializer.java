package com.vladimir.crudblog.repository.gson.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vladimir.crudblog.model.Post;

import java.lang.reflect.Type;
import java.util.List;

public class JsonPostsSerializer implements JsonSerializer<List<Post>> {
    @Override
    public JsonElement serialize(List<Post> postList, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(postList.stream()
                .map(post -> post.getId().toString())
                .reduce("", (allIds, Id) -> allIds + " " + Id)
                .trim());
    }
}
