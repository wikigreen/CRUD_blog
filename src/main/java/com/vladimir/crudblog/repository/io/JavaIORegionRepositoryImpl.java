package com.vladimir.crudblog.repository.io;

import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.GenericRepository;
import com.vladimir.crudblog.repository.RegionRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaIORegionRepositoryImpl implements RegionRepository {
    private final String REGIONS_FILE_PATH = "src//main//resources//files//regions.txt";
    private static JavaIORegionRepositoryImpl instance;
    private final File file;
    private final BufferedWriter bufferedWriter;
    private Long lastId;


    private JavaIORegionRepositoryImpl() throws IOException {
        file = new File(REGIONS_FILE_PATH);
        bufferedWriter = new BufferedWriter(new FileWriter(file, true));
    }

    public static JavaIORegionRepositoryImpl getInstance() {
        if (instance == null) {
            try {
                instance = new JavaIORegionRepositoryImpl();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public Region save(Region region) {
        if (region.getId() == null)
            region.setId(generateId());
        try {
            bufferedWriter.write(repositoryToString(region) + System.lineSeparator());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return region;
    }

    public Region update(Region region) {
        streamOfRegions()
                .filter(p -> p.getId().equals(region.getId()))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no region with id " + region.getId() + " in repository"));


        long id = region.getId();
        List<Region> lines = streamOfRegions().collect(Collectors.toList());
        clear();
        lines.forEach((r) -> {
                    try {
                        if (r.getId() == id){
                            bufferedWriter.write(repositoryToString(region) + System.lineSeparator());
                        }
                        else {
                            bufferedWriter.write(repositoryToString(r) + System.lineSeparator());
                        }
                        bufferedWriter.flush();
                    } catch (IOException e) {
                        System.err.println("An error while updating");
                    }
                });
        return region;
    }

    @Override
    public Region getById(Long id) {
        return streamOfRegions()
                .filter(p -> p.getId().equals(id))
                .findAny().orElse(null);
    }

    public void deleteById(Long id) {
        streamOfRegions()
                .filter(p -> p.getId().equals(id))
                .findAny().orElseThrow(() -> new IllegalArgumentException("There is no region with id " + id + " in repository"));

        List<Region> lines = streamOfRegions().collect(Collectors.toList());
        clear();
        lines.forEach((r) -> {
                    try {
                        if (id != r.getId()) {
                            bufferedWriter.write(repositoryToString(r) + System.lineSeparator());
                            bufferedWriter.flush();
                        }
                    } catch (IOException e) {
                        System.err.println("An error while deleting");
                    }
                });
    }

    public List<Region> getAll() {
        return streamOfRegions().collect(Collectors.toList());
    }

    private static String repositoryToString(Region region) {
        if (region == null)
            throw new IllegalArgumentException("argument \"region\" can not be null");
        if (region.getId() == null)
            throw new IllegalArgumentException("ID can not be null");
        return region.getId() + "," + region.getName();
    }

    private void clear() {
        try {
            new PrintWriter(file).close();
        } catch (IOException e) {
            System.err.println("Repository has not been cleared");
        }
    }

    public static Region parseRegion(String region) {
        // Checking if region can be parsed to a Region
        if (!region.matches("\\d+,.+"))
            throw new IllegalArgumentException("Line " + region + " can not be parsed to Region");
        String[] lines = region.split(",", 2);
        return new Region(Long.parseLong(lines[0]), lines[1]);
    }

    private Stream<Region> streamOfRegions() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return br.lines().map(JavaIORegionRepositoryImpl::parseRegion);
        } catch (IOException e) {
            System.err.println();
        }
        throw new Error("An error while reading from file");

    }

    private Long generateId() {
        if(this.lastId == null){
            lastId = streamOfRegions()
                    .map(Region::getId)
                    .max(Long::compareTo)
                    .orElse(0L);
        }
        return ++lastId;
    }


}
