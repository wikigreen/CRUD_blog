package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.repository.RegionRepository;

import java.util.List;

public class RegionController {
    private static RegionRepository repository = RegionRepository.getInstance();

    public static List<Region> getAll(){
        return repository.getAll();
    }

    public static Region addRegion(String name){
        Region region = new Region(null, name);
        repository.save(region);
        return region;
    }

    public static Region getByID(Long id){
        return repository.getById(id);
    }


    public static void deleteByID(Long id) {
        repository.deleteById(id);
    }

    public static Region update(Region region){
        repository.update(region);
        return region;
    }

}
