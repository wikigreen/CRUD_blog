package view;

import model.Region;
import repository.RegionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args){
        RegionRepository repository = RegionRepository.getInstance();
        //for(int i = 0; i < 10; i++) repository.save(new Region(null, "Ukraine"));
        repository.getRegions().stream().forEach(r -> repository.deleteById(r.getId()));
    }
}
