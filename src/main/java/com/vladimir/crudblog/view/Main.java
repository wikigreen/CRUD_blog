package com.vladimir.crudblog.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;

public class Main {
    private static boolean isRunning = true;
    public static void main(String[] args) {
        System.out.println("Type 'commands' to see all commands.");
        while(isRunning){
            System.out.print("Type command:");
            executeCommand();
            System.out.println();
        }
        System.out.println("See you!!!");
    }

    private static void executeCommand(){
        String[] tempCommands = ConsoleHelper.readLine().trim().split(" ");
        String[] commands = {tempCommands[0], "", ""};

        try {commands[1] = tempCommands[1];} catch (ArrayIndexOutOfBoundsException ignored) {}
        View view = switch (commands[1]){
            case "region": yield new RegionView();
            case "post": yield new PostView();
                //case "user":
            default: yield null;
        };

        try {commands[2] = tempCommands[2];} catch (ArrayIndexOutOfBoundsException ignored) {}
        Long id = null;
        try{id = Long.parseLong(commands[2]);} catch (NumberFormatException ignored){}

        try{
            switch (commands[0]) {
                case ""            -> {}
                case "exit"        -> isRunning = false;
                case "commands"    -> executeCommands();
                case "create"      -> view.create();
                case "read_all"    -> view.readAll();
                case "read"        -> view.read(id);
                case "update"      -> view.update(id);
                case "delete"      -> view.delete(id);
                default            -> System.out.println("'" + commands[0] + "'" + " is not a command" +
                        "\n" + "Type 'commands' to see all commands.");
            }
        } catch (NullPointerException e){
            //if there is no second part of command (should be followed by)
            if("".equals(commands[1]))
                System.out.println("After command '" + commands[0] + "' should be a type of object ('region', 'post' or 'user') and id, if necessary" + "\n"
                + "For example: create region");
            else
                System.out.println("Type '" + commands[1] + "' is not supported, only 'region', 'post' or 'user' allowed");
        } catch (IllegalArgumentException e1){
            //if there is no id
            if("".equals(commands[2]))
                System.out.println("After command '" + commands[0] + " " + commands[1] + "' should be an ID" + "\n"
                        + "For example: delete region 1");
            else
                System.out.println(commands[2] + " is not a number");
        }


    }

    private static void executeCommands() {
        System.out.println("Not supported yet");
    }


    static void clearReps(){
       try {
           new PrintWriter("src//main//resources//files//posts.txt").close();
           new PrintWriter("src//main//resources//files//regions.txt").close();
           new PrintWriter("src//main//resources//files//users.txt").close();
       } catch (IOException e) {
           System.err.println("Repository has not been cleared");
       }
   }


}
