
package System;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Main {
    public static void main(String[]args) throws IOException {
        SensorSystem sensor = new SensorSystem();
        sensor.readFile("src/main/java/System/floorplan2.txt");
        ControlSystem control = new ControlSystem(sensor);
        Scanner in = new Scanner(System.in);
        System.out.println("Clean Sweep starting...");
        System.out.println("Type 'log' for a log of the movements of the machine...");

        String needLog = in.nextLine();

        // Creating a PrintStream variable to store the printout stream to a File object
        PrintStream toFile = new PrintStream(new File("src/main/java/System/log.txt"));

        // this PrintStram variable is store the default System.out
        PrintStream toConsole = System.out;

        if(needLog.toLowerCase().equals("log")) {
                while(sensor.getVisitedTiles()<sensor.getTotalTiles()-1 && !control.emptystopFlag && control.moveDevice() ) {
                    control.moveDevice();
                }
                //call this method to write logs to a file and printout logs to console at the same time
                control.printLog(toFile,toConsole);
            }

        if(!needLog.toLowerCase().equals("log")) {
                System.out.println("Type valid command please");
            }

        }
    }

