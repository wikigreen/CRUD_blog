package com.vladimir.crudblog.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static String readLine(){
        while (true){
            try {
                return br.readLine();
            } catch (IOException e) {
                System.err.println("An error occurred while trying to read from console. Please try again:");
            }
        }
    }

    public static Long readLong(){
        String str = "";
        while (true){
            try {
                str = readLine();
                return Long.parseLong(str);
            } catch (NumberFormatException e){
                System.err.println("\"" + str + "\"" + " is not a number. Please try again:");
            }
        }
    }


}
