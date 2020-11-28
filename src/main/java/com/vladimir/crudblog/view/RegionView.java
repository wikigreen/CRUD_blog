package com.vladimir.crudblog.view;

import com.vladimir.crudblog.controller.RegionController;
import com.vladimir.crudblog.model.Region;

import java.util.List;

public class RegionView implements View{

    @Override
    public void create(){
        System.out.print("Enter name of the new region:");
        System.out.println("Added new region "
                + RegionController.addRegion(ConsoleHelper.readLine()).toString());
    }

    @Override
    public void readAll(){
        List<Region> regions = RegionController.getAll();

        if(regions.size() == 0){
            System.out.println("No regions in repository");
            return;
        }
        System.out.println("List of all regions:");
        regions.stream().map(Region::toString)
                .forEachOrdered(System.out::println);
    }

    @Override
    public void read(Long id){
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }
        Region region = RegionController.getByID(id);
        if(region == null){
            System.out.println("There is no region with id " + id);
            return;
        }
        System.out.println(region);
    }

    @Override
    public void update(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }
        System.out.print("Type new region name:");
        Region region = new Region(id, ConsoleHelper.readLine().trim());
        try {
            RegionController.update(region);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Region with ID " + id + " has been changed successfully");

    }

    @Override
    public void delete(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        try{
            RegionController.deleteByID(id);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Region with id " + id + " has been deleted successfully");
    }
}
