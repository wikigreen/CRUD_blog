package com.vladimir.crudblog.repository.gson.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.repository.gson.JsonRegionRepositoryImpl;

import java.lang.reflect.Type;

public class JsonRegionDeserializer implements JsonDeserializer<Region> {
    @Override
    public Region deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return JsonRegionRepositoryImpl.getInstance()
                .getById(jsonElement.getAsLong());
    }
}
