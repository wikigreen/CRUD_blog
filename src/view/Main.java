package view;

import model.Region;
import repository.RegionRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        RegionRepository repository = null;
        repository = RegionRepository.getInstance();
        repository.getRegions().stream().forEach(System.out::println);
//        repository.getRegions().add(new Region(11, "USA"));
//        repository.getRegions().add(new Region(12, "UA"));
//        repository.getRegions().add(new Region(13, "RU"));
//        repository.getRegions().add(new Region(14, "EU"));
        try {
            repository.flush();  
        } catch (IOException e) {
            System.err.println("An error with writing in file");
        }


    }
}
