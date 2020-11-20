package repository;

import model.Region;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegionRepository {
    private static RegionRepository instance;
    private File file;
    private BufferedWriter bufferedWriter;
    private Long lastId;


    private RegionRepository() throws IOException {
        file = new File("src//repository//regions.txt");
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        if(streamOfRegions().count() == 0){
            lastId = (long)0;
            return;
        }
        final Long[] lastId = {(long)-1};
        streamOfRegions().forEachOrdered((r -> lastId[0] = r.getId()));
        this.lastId = lastId[0];
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

    public Region save(Region region) throws IOException {
        if(region.getId() == null)
            region.setId(++this.lastId);
        bufferedWriter.write(repositoryToString(region) + System.lineSeparator());
        bufferedWriter.flush();
        return region;
    }

    public Region update(Region region){
        long id = region.getId();
        List<Region> lines = streamOfRegions().collect(Collectors.toList());
        clear();
        lines.stream()
                .forEach((r) -> {
                    try {
                        if(r.getId() == id)
                            save(region);
                        else{
                            save(r);
                        }
                    } catch (IOException e) {
                        System.err.println("An error while updating");
                    }
        });
        return region;
    }

    public void deleteById(long id){
        List<Region> lines = streamOfRegions().collect(Collectors.toList());

        clear();
        lines.stream()
                .forEach((r) ->{
                    try {
                        if(id != r.getId()){
                            save(r);
                         }

                    } catch (IOException e) {
                        System.err.println("An error while deleting");
                    }
        });
    }

    public List<Region> getRegions() {
        return streamOfRegions().collect(Collectors.toList());
    }

    public static String repositoryToString(Region region){
        if (region == null)
            throw new IllegalArgumentException("argument \"region\" can not be null");
        return region.getId() + "," + region.getName();
    }

    private void clear(){
        try {
            new PrintWriter(file).close();
        } catch (IOException e) {
            System.err.println("Repository has not been cleared");
        }
    }

    public static Region parseRegion(String region){
        // Checking if region can be parsed to a Region
        if(!region.matches("\\d+,.+"))
            throw new IllegalArgumentException("Line "+ region + " can not be parsed to Region");
        String[] lines = region.split(",", 2);
        return new Region(Long.parseLong(lines[0]), lines[1]);
    }

    private Stream<Region> streamOfRegions() {
        try{
            BufferedReader br = new BufferedReader(new FileReader("src//repository//regions.txt"));
            return br.lines().map(RegionRepository::parseRegion);
        }  catch (IOException e) {
            System.err.println();
        }
        throw new Error("An error while reading from file");

    }



}
