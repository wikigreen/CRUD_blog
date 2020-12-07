package com.vladimir.crudblog.view;

import com.vladimir.crudblog.controller.UserController;
import com.vladimir.crudblog.model.Post;
import com.vladimir.crudblog.model.Region;
import com.vladimir.crudblog.model.Role;
import com.vladimir.crudblog.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserView implements View {
    UserController userController = new UserController();
    private final Scanner SCANNER = new Scanner(System.in);

    @Override
    public void create() {
        System.out.print("Type first name:");
        String firstName = SCANNER.nextLine().trim();
        System.out.print("Type last name:");
        String lastName = SCANNER.nextLine().trim();

        Region region;
        //Trying to get region
        while(true){
            System.out.println("Type 'new <name>' to add new region, or 'existing <id>' to add region from repository:");
            String[] splitCommands = SCANNER.nextLine().trim().split(" +");
            if(splitCommands.length < 2){
                System.out.println("'" + splitCommands[0] + "' command can not be correctly processed");
                continue;
            }
            if("new".equals(splitCommands[0])){
                region = new Region(null, splitCommands[1]);
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
                if(id.compareTo(0L) < 1){
                    System.out.println("ID always should be greater than 0");
                    continue;
                }
                region = new Region(id, null);
                break;
            }
        }

        //Trying to get role
        Role role;
        while(true){
            System.out.println("Type role (user, moder or admin):");
            String strRole = SCANNER.nextLine().trim();
            try{
                role = Role.parseRole(strRole);
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }

        //Trying to get posts
        ArrayList<Post> posts = new ArrayList<>();
        while(true){
            System.out.print("Type 'new' to add new post, or 'existing <id>' to add region from repository.\n" +
                    "To stop adding posts press 'Enter' button:");
            String command = SCANNER.nextLine().trim();
            if("".equals(command))
                break;
            if("new".equals(command)){
                System.out.print("Type post, in the end type '%end' to save it:");
                String content = "";
                String line;
                while(!(line = SCANNER.nextLine().trim()).equals("%end"))
                    content += line + "\n";
                if (content.length() == 0) content += " ";
                Post post = new Post(null, content);
                posts.add(post);
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

            Post post = new Post(parsedId, null);
            posts.add(post);
        }
        User user = userController.addUser(firstName, lastName, posts, region, role);

        if(user.getRegion() == null)
            System.out.println("Region with id " + region.getId() + " does not exist");
        for(Post post : posts){
            Boolean isPostMissed = user.getPosts().stream()
                                        .filter(p -> p.getId().equals(post.getId()))
                                        .map(p -> Boolean.FALSE)
                                        .findAny().orElse(Boolean.TRUE);
            if(isPostMissed)
                System.out.println("Post with id " + post.getId() + " does not exist");
        }
        System.out.println();
        System.out.println("User has been added to repository:\n" + user.toString());

    }

    @Override
    public void readAll() {
        List<User> users = userController.getAll();

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
        if(id.compareTo(0L) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        User user = userController.getByID(id);
        if(user == null){
            System.out.println("There is no post with id " + id);
            return;
        }
        System.out.println(user);
    }

    @Override
    public void update(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo(0L) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }

        if(!userController.exists(id)){
            System.out.println("User with ID " + id + " does not exists");
            return;
        }


        System.out.print("Type new first name. Press 'Enter' to skip:");
        String firstName = SCANNER.nextLine().trim();
        if("".equals(firstName))
            firstName = null;
        System.out.print("Type new last name. Press 'Enter' to skip:");
        String lastName = SCANNER.nextLine().trim();
        if("".equals(lastName))
            lastName = null;

        Region region = null;
        //Trying to get region
        while(true){
            System.out.print("Type 'new <name>' to add new region, or 'existing <id>' to add region from repository\n"
                                + "Press 'Enter' to skip:");
            String[] splitCommands = SCANNER.nextLine().trim().split(" +");
            if("".equals(splitCommands[0]))
                break;

            if(splitCommands.length < 2){
                System.out.println("'" + splitCommands[0] + "' command can not be correctly processed");
                continue;
            }

            if("new".equals(splitCommands[0])){
                region = new Region(null, splitCommands[1]);
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
                if(regionId.compareTo(0L) < 1){
                    System.out.println("ID always should be greater than 0");
                    continue;
                }
                region = new Region(regionId, null);
                break;
            }
        }

        Role role = null;
        while(true){
            System.out.println("Type role (user, moder or admin). Press 'Enter' to skip:");
            String strRole = SCANNER.nextLine().trim();
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
            String[] strIDsToAdd = SCANNER.nextLine().trim().split(",");
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
            String[] strIDsToDelete = SCANNER.nextLine().trim().split(",");
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

        User updatedUser = userController.update(id, firstName, lastName, region, role, postsToAdd, postsToDelete);
        if(region != null && updatedUser.getRegion() == null)
            System.out.println("Region with id " + region.getId() + " does not exist");
        System.out.println("User with ID " + updatedUser.getId() + " has been updated successfully:");
        System.out.println(updatedUser);

    }

    @Override
    public void delete(Long id) {
        if(id == null) throw new IllegalArgumentException();
        if(id.compareTo((long)0) < 1){
            System.out.println("ID always should be greater than 0");
            return;
        }
        try{
            userController.deleteByID(id);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("User with id " + id + " has been deleted successfully");
    }
}
