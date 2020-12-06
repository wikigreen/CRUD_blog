package com.vladimir.crudblog.controller;

import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.repository.RegionRepository;
import com.vladimir.crudblog.repository.io.JavaIORegionRepositoryImpl;

import java.util.List;

public class RegionController {
    private static RegionRepository repository = JavaIORegionRepositoryImpl.getInstance();

    public List<Region> getAll(){
        return repository.getAll();
    }

    public Region addRegion(String name){
        Region region = new Region(null, name);
        repository.save(region);
        return region;
    }

    public Region getByID(Long id){
        return repository.getById(id);
    }


    public void deleteByID(Long id) {
        repository.deleteById(id);
    }

    public Region update(Region region){
        repository.update(region);
        return region;
    }

}
