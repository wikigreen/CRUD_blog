package repository;

import model.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RegionRepository {
    private List<Region> regions;
    private static RegionRepository instance;

    private RegionRepository() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("src//repository//regions.txt"));
        regions = new ArrayList<>();
        fileReader.lines()
                .forEachOrdered((line) -> {
                String[] lines = line.split(",", 2);
                regions.add(new Region(Long.parseLong(lines[0]), lines[1]));
                });
        fileReader.close();
    }

    public static RegionRepository getInstance() {
        if(instance == null) {
            try {
                instance = new RegionRepository();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public Region save(Region region){
        regions.add(region);
        return region;
    }

    public Region update(Region region){
        regions.stream()
                .filter((r) -> r.getId() == region.getId())
                .forEach((a) -> {
                    regions.add(regions.indexOf(a), region);
                });
        return region;
    }

    public void deleteById(long id){
        regions.stream().filter((r) -> r.getId() == id).forEach(regions::remove);
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void flush() throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("src//repository//regions.txt"));
        regions.stream().map(Region::toString).forEach(str -> {
            try {
                fileWriter.write(str + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }


}
