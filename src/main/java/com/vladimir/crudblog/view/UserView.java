package com.vladimir.crudblog.view;

import com.vladimir.crudblog.controller.PostController;
import com.vladimir.crudblog.controller.UserController;
import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;
import com.vladimir.crudblog.repository.RegionRepository;

import java.util.ArrayList;
import java.util.List;

public class UserView implements View {
    RegionRepository regionRepository = RegionRepository.getInstance();
    @Override
    public void create() {
        System.out.print("Type first name:");
        String firstName = ConsoleHelper.readLine().trim();
        System.out.print("Type last name:");
        String lastName = ConsoleHelper.readLine().trim();

        Region region;
        //Trying to get region
        while(true){
            System.out.println("Type 'new <name>' to add new region, or 'existing <id>' to add region from repository:");
            String[] splitCommands = ConsoleHelper.readLine().trim().split(" +");
            if(splitCommands.length < 2){
                System.out.println("'" + splitCommands[0] + "' command can not be correctly processed");
                continue;
            }
            if("new".equals(splitCommands[0])){
                region = regionRepository.save(new Region(null, splitCommands[1]));
                break;
            }
            if("existing".equals(splitCommands[0])){
                Long id;
                try{
                    id = Long.parseLong(splitCommands[1]);
                } catch (NumberFormatException e){
                    System.out.println(splitCommands[1] + " is not a number");
                    continue;
                }
                if(id.compareTo((long)0) < 1){
                    System.out.println("ID always should be greater than 0");
                    continue;
                }
                region = regionRepository.getById(id);
                if (region == null){
                    System.out.println("Region with id " + id + " does not exist");
                    continue;
                }
                break;
            }
        }

        //Trying to get role
        Role role;
        while(true){
            System.out.println("Type role (user, moder or admin):");
            String strRole = ConsoleHelper.readLine().trim();
            try{
                role = Role.parseRole(strRole);
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        //Trying to get posts
        ArrayList<Post> posts = new ArrayList<Post>();
        while(true){
            System.out.print("Type 'new' to add new post, or 'existing <id>' to add region from repository.\n" +
                    "To stop adding posts press 'Enter' button:");
            String command = ConsoleHelper.readLine().trim();
            if("".equals(command))
                break;
            if("new".equals(command)){
                System.out.print("Type post, in the end type '%end' to save it:");
                String strPost = "";
                String line;
                while(!(line = ConsoleHelper.readLine()).equals("%end"))
                    strPost += line + "\n";
                if (strPost.length() == 0) strPost += " ";
                Post post = PostController.addPost(strPost);
                posts.add(post);
                System.out.println("Added new Post with ID " + post.getId());
                continue;
            }
            String[] commands = command.split(" +");
            if(commands.length != 2){
                System.out.println("'" + command + "' command can not be correctly processed");
                continue;
            }
            if(!"existing".equals(commands[0])){
                System.out.println("'" + commands[0] + "' command can not be correctly processed");
                continue;
            }
            Long parsedId;
            try{
                parsedId = Long.parseLong(commands[1]);
            } catch (NumberFormatException e){
                System.out.println(commands[1] + " is not a number");
                continue;
            }
            if(parsedId.compareTo((long)0) <= 0){
                System.out.println("ID always have to be greater then 0");
                continue;
            }

            Post post = PostController.getByID(parsedId);
            if(post == null){
                System.out.println("Post with ID " + parsedId + " does not exist");
                continue;
            }
            posts.add(post);
            System.out.println("Added new Post with ID " + post.getId());
        }
        UserController.addUser(firstName, lastName, posts, region, role);
    }

    @Override
    public void readAll() {
        List<User> users = UserController.getAll();

        if(users.size() == 0){
            System.out.println("No users in repository");
            return;
        }
        System.out.println("List of all users:");
        users.stream().map(User::toString)
                .map(s -> s + "\n")
                .forEachOrdered(System.out::println);
    }

    @Override
    public void read(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        User user = UserController.getByID(id);
        if(user == null){
            System.out.println("There is no post with id " + id);
            return;
        }
        System.out.println(user);
    }

    @Override
    public void update(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        if(!UserController.exists(id)){
            System.out.println("User with ID " + id + " does not exists");
            return;
        }


        System.out.print("Type new first name. Press 'Enter' to skip:");
        String firstName = ConsoleHelper.readLine().trim();
        if("".equals(firstName))
            firstName = null;
        System.out.print("Type new last name. Press 'Enter' to skip:");
        String lastName = ConsoleHelper.readLine().trim();
        if("".equals(lastName))
            lastName = null;

        Region region = null;
        //Trying to get region
        while(true){
            System.out.print("Type 'new <name>' to add new region, or 'existing <id>' to add region from repository\n"
                                + "Press 'Enter' to skip:");
            String[] splitCommands = ConsoleHelper.readLine().trim().split(" +");
            if("".equals(splitCommands[0]))
                break;

            if(splitCommands.length < 2){
                System.out.println("'" + splitCommands[0] + "' command can not be correctly processed");
                continue;
            }

            if("new".equals(splitCommands[0])){
                region = regionRepository.save(new Region(null, splitCommands[1]));
                break;
            }
            if("existing".equals(splitCommands[0])){
                Long regionId;
                try{
                    regionId = Long.parseLong(splitCommands[1]);
                } catch (NumberFormatException e){
                    System.out.println(splitCommands[1] + " is not a number");
                    continue;
                }
                if(regionId.compareTo((long)0) < 1){
                    System.out.println("ID always should be greater than 0");
                    continue;
                }
                region = regionRepository.getById(regionId);
                if (region == null){
                    System.out.println("Region with id " + regionId + " does not exist");
                    continue;
                }
                break;
            }
        }

        Role role = null;
        while(true){
            System.out.println("Type role (user, moder or admin). Press 'Enter' to skip:");
            String strRole = ConsoleHelper.readLine().trim();
            if("".equals(strRole))
                break;
            try{
                role = Role.parseRole(strRole);
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        List<Long> postsToAdd = new ArrayList<>();
        add: while(true){
            System.out.print("Write comma-separated list of posts ID to add them:");
            String[] strIDsToAdd = ConsoleHelper.readLine().trim().split(",");
            if("".equals(strIDsToAdd[0]))
                break;
            for(String strId : strIDsToAdd){
                try{
                    postsToAdd.add(Long.parseLong(strId.trim()));
                } catch (NumberFormatException e){
                    System.out.println("'" + strId.trim() + "' is not a number. Try again");
                    postsToAdd.clear();
                    continue add;
                }
            }
            break;
        }

        List<Long> postsToDelete = new ArrayList<>();
        delete: while(true){
            System.out.print("Write comma-separated list of posts ID to delete them:");
            String[] strIDsToDelete = ConsoleHelper.readLine().trim().split(",");
            if("".equals(strIDsToDelete[0]))
                break;
            for(String strId : strIDsToDelete){
                try{
                    postsToDelete.add(Long.parseLong(strId.trim()));
                } catch (NumberFormatException e){
                    System.out.println(strId.trim() + " is not a number. Try again");
                    postsToDelete.clear();
                    continue delete;
                }
            }
            break;
        }

        User updatedUser = UserController.update(id, firstName, lastName, region, role, postsToAdd, postsToDelete);
        System.out.println("User with ID " + updatedUser.getId() + " has been updated successfully");
    }

    @Override
    public void delete(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        try{
            UserController.deleteByID(id);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("User with id " + id + " has been deleted successfully");
    }
}
