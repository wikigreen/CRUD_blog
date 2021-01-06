package com.vladimir.crudblog.repository.gson.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.vladimir.crudblog.model.Region;

import java.lang.reflect.Type;

public class JsonRegionSerializer implements JsonSerializer<Region> {
    @Override
    public JsonElement serialize(Region region, Type type, JsonSerializationContext jsonSerializationContext) {
        if(region == null)
            return new JsonPrimitive(0L);
        return new JsonPrimitive(region.getId());
    }
}
