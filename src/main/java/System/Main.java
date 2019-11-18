package System;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[]args) throws IOException {
        SensorSystem sensor = new SensorSystem();
        Scanner readFile = new Scanner(System.in);
        System.out.println("Welcome to Clean Sweep!");
        System.out.println("Enter the path to your floor plan:");
        String path = "";
        if(readFile.hasNextLine()) {
            path = readFile.nextLine();
        }
        sensor.readFile(path);
        ControlSystem control = new ControlSystem(sensor);
        Scanner in = new Scanner(System.in);
        System.out.println("Clean Sweep starting...");
        System.out.println("Type 'log' for a log of the movements of the machine...");
        while(sensor.getVisitedTiles()<sensor.getTotalTiles()-1 && control.moveDevice()) {
            control.moveDevice();
            String needLog = in.nextLine();
            if(needLog.toLowerCase().equals("log")) {
                control.printLog();
            }
            if(!needLog.toLowerCase().equals("log")) {
                System.out.println("Type valid command please");
            }
        }
        in.close();
        readFile.close();
    }
}