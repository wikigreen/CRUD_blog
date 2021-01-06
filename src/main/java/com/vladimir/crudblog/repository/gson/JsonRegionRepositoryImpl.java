package com.vladimir.crudblog.repository.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.repository.RegionRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonRegionRepositoryImpl implements RegionRepository {
    private final String REGION_JSON_FILE_PATH = "src//main//resources//json//regions.json";
    private static JsonRegionRepositoryImpl instance = new JsonRegionRepositoryImpl();
    private final Gson gson;
    private final File file;
    private Long lastId;

    public static JsonRegionRepositoryImpl getInstance(){
        return instance;
    }

    private JsonRegionRepositoryImpl(){
        file = new File(REGION_JSON_FILE_PATH);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private List<Region> listOfRegions() {
        try {
            JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
            List<Region> regionList = gson.fromJson(jsonReader, new TypeToken<List<Region>>(){}.getType());
            if(regionList == null)
                return new ArrayList<Region>();
            return regionList;
        } catch (FileNotFoundException e) {
            System.out.println("An error while reading from file");
        }
        throw new Error("Can not read from regions.json");
    }

    private void writeList(List<Region> regionList){
        try {
            JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(file)));
            jsonWriter.setIndent("  ");
            gson.toJson(regionList, new TypeToken<List<Region>>(){}.getType(), jsonWriter);
            jsonWriter.flush();
        } catch (IOException e) {
            System.out.println("File " + file + " is not found");
        }
    }

    @Override
    public Region save(Region region) {
        if (region.getId() == null)
            region.setId(generateId());
        List<Region> regions = listOfRegions();
        regions.add(region);
        writeList(regions);
        return region;
    }

    @Override
    public Region update(Region region) {
        List<Region> regionList = listOfRegions();

        Region regionToUpd = regionList.stream()
                .filter(reg -> reg.getId().equals(region.getId()))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no region with id " + region.getId() + " in repository"));

        regionToUpd.setName(region.getName());
        writeList(regionList);

        return region;
    }


    @Override
    public Region getById(Long id) {
        return listOfRegions().stream()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        List<Region> regionList = listOfRegions();
        if(!regionList.removeIf(region -> region.getId().equals(id))) {
            throw new IllegalArgumentException("There is no region with id " + id + " in repository");
        }
        writeList(regionList);
    }

    @Override
    public List<Region> getAll() {
        return listOfRegions();
    }

    private Long generateId() {
        if(this.lastId == null){
            lastId = listOfRegions()
                    .stream()
                    .map(Region::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }
}
