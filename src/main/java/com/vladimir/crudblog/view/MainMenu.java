package com.vladimir.crudblog.view;

import java.util.Scanner;

public class MainMenu {
    private static boolean isRunning = true;
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Type 'help' to see all commands.");
        while(isRunning){
            System.out.print("Type command:");
            executeCommand();
            System.out.println();
        }
        System.out.println("See you!!!");
    }

    private void executeCommand(){
        String[] tempCommands = scanner.nextLine().trim().split(" +");
        String[] commands = {tempCommands[0], "", ""};

        try {commands[1] = tempCommands[1];} catch (ArrayIndexOutOfBoundsException ignored) {}
        View view = switch (commands[1]){
            case "region": yield new RegionView();
            case "post": yield new PostView();
            case "user": yield new UserView();
            default: yield null;
        };

        try {commands[2] = tempCommands[2];} catch (ArrayIndexOutOfBoundsException ignored) {}
        Long id = null;
        try{id = Long.parseLong(commands[2]);} catch (NumberFormatException ignored){}

        try{
            switch (commands[0]) {
                case ""            -> {}
                case "exit"        -> isRunning = false;
                case "help"        -> executeHelp();
                case "create"      -> view.create();
                case "read_all"    -> view.readAll();
                case "read"        -> view.read(id);
                case "update"      -> view.update(id);
                case "delete"      -> view.delete(id);
                default            -> System.out.println("'" + commands[0] + "'" + " is not a command" +
                        "\n" + "Type 'help' to see all commands.");
            }
        } catch (NullPointerException e){
            if("".equals(commands[1]))
                System.out.println("After command '" + commands[0] + "' should be a type of object ('region', 'post' or 'user') and id, if necessary" + "\n"
                        + "For example: '" + commands[0] + "' region");
            else
                System.out.println("Type '" + commands[1] + "' is not supported, only 'region', 'post' or 'user' allowed");
        } catch (IllegalArgumentException e1){
            if("".equals(commands[2]))
                System.out.println("After command '" + commands[0] + " " + commands[1] + "' should be an ID" + "\n"
                        + "For example: '" + commands[0] + " " + commands[1] + " 1'");
            else
                System.out.println(commands[2] + " is not a number");
        }


    }

    private static void executeHelp() {
        System.out.println("Types of data objects, that can be handled:\n" +
                "\tregion\t\tRepresents name of region. Used with \"user\" type.\n" +
                "\t\t\t\tContains 'id', that sets automatically and 'name', that can be set by user.\n" +
                "\n" +
                "\tpost\t\tRepresents user`s post.\n" +
                "\t\t\t\tContains 'id', that sets automatically, 'content', that can be set by user,\n" +
                "\t\t\t\t'creation date' and 'modification date'.\n" +
                "\n" +
                "\tuser\t\tRepresents user. \n" +
                "\t\t\t\tContains 'id', that sets automatically, users first and last name, 'role' (user,\n" +
                "\t\t\t\tmoderator or admin), 'region' type (can be get from DB or created during the \n" +
                "\t\t\t\tmaking user), list of 'posts' type, (can be added from DB or created during the \n" +
                "\t\t\t\tmaking user).\n" +
                "\n" +
                "List of commands:\n" +
                "\tcreate <type>\t\tCreates and saves one of supported types to DB.\n" +
                "\t\t\t\t\t\tFor example: \"create region\".\n" +
                "\n" +
                "\tread_all <type>\t\tPrints list of all objects of the specified <type>.\n" +
                "\t\t\t\t\t\tFor example: \"read_all region\".\n" +
                "\n" +
                "\tread <type> <id>\tPrints specific object of type <type> with ID <id>. <id> is number\n" +
                "\t\t\t\t\t\tgreater than 0. For example: \"read region 1\".\n" +
                "\n" +
                "\tupdate <type> <id>\tUpdates specific object of type <type> with ID <id>.\n" +
                "\t\t\t\t\t\tFor example: \"update region 1\".\n" +
                "\n" +
                "\tdelete <type> <id>\tDeletes specific object of type <type> with ID <id> from DB.\n" +
                "\t\t\t\t\t\tFor example: \"delete region 1\".\n");
    }
}
