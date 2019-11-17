package System;

import java.util.*;
import java.io.*;
import System.findPath.Cell;

public class ControlSystem {

    //instance variables
    static SensorSystem sensor;
    float batteryLevel = 100;
    int[] position = new int[2];
    boolean rechargeMode = false;
    boolean emptydirtMode = false;
    boolean emptystopFlag = false;
    Stack<Integer[]> rechargePathStack = new Stack<>();//stack used for moving back from charge station to the last visited tile
    Queue<Integer[]> rechargePathQueue = new LinkedList<>();//queue used for moving to charge station
    Queue<Integer[]> emptydirtPathQueue = new LinkedList<>();//queue used for moving to charge station
    Stack<Integer[]> emptydirtPathStack = new Stack<>();//stack used for moving back from charge station
    Stack<Integer[]> previousPath = new Stack<>(); //Stack for basic movement (obstacle recognition)
    ArrayList<String> log = new ArrayList<>();

    boolean moveBack = false; //flag for whether the machine is moving back or not
    int dirtCapacity = 100;// set value to 10 to test the 'empty me' functionality
    int dirtLoad = 0;

    public ControlSystem(SensorSystem sensor) {
        this.sensor = sensor;
        position[0] = 0;
        position[1] = 0;
    }

    public int[] checkWhereToMove(){
        //where next to move
        int[] coordinates = new int[2];

        //cleaning mode moving algorithm
        if (!rechargeMode & !emptydirtMode) {
            //check if anything is down (per our POV)
            if (!sensor.isObstacle(position[0], position[1] + 1) && !sensor.isVisited(position[0], position[1] + 1)) {
                log.add("Checking " + position[0] + ", " + (position[1] + 1));
                coordinates[0] = 0;
                coordinates[1] = 1;
                moveBack = false;
            }
            //check if anything is in the right (per our POV)
            else if (!sensor.isObstacle(position[0] + 1, position[1]) && !sensor.isVisited(position[0] + 1, position[1])) {
                log.add("Checking " + (position[0] + 1) + ", " + position[1]);
                coordinates[0] = 1;
                coordinates[1] = 0;
                moveBack = false;
            }
            //check if anything is on the left (per our POV)
            else if (!sensor.isObstacle(position[0] - 1, position[1]) && !sensor.isVisited(position[0] - 1, position[1])) {
                log.add("Checking " + (position[0] - 1) + ", " + position[1]);
                coordinates[0] = -1;
                coordinates[1] = 0;
                moveBack = false;
            } else if (!sensor.isObstacle(position[0], position[1] - 1) && !sensor.isVisited(position[0], position[1] - 1)) {
                log.add("Checking " + (position[0]) + ", " + (position[1] - 1));
                coordinates[0] = 0;
                coordinates[1] = -1;
                moveBack = false;
            } else {
                previousPath.pop();
                Integer[] coordinatesFromStack = previousPath.pop();
                coordinates[0] = coordinatesFromStack[0].intValue();
                coordinates[1] = coordinatesFromStack[1].intValue();
                moveBack = true;
            }
            return coordinates;
        }

        //moving algorithm for recharge mode, moving back to the charging station(0,0)
        else if(rechargeMode) {
            //pop the location from queue to get the next step location, then calculate the coordinates(moving direction)
            Integer[] temp = rechargePathQueue.poll();
            coordinates[0] = temp[0]-position[0];
            coordinates[1] = temp[1]-position[1];
            return coordinates;
        }

        else if(emptydirtMode){
            Integer[] temp = emptydirtPathQueue.poll();
            coordinates[0] = temp[0]-position[0];
            coordinates[1] = temp[1]-position[1];
            return coordinates;
        }

        else return null;
    }


    public boolean moveDevice(){
        int[] whereToGo = this.checkWhereToMove();
        Integer[] whereToGoStack = new Integer[2];
        //if it has not traverse the whole floor and not in recharge mode, it means it is in cleaning mode
        if (whereToGo[0] != Integer.MAX_VALUE & !rechargeMode & !emptydirtMode) {
            if (!moveBack) {
                //if you are not moving back, we are just adding the directions to the position of the machine
                setBatteryLevel(position[0], position[1], position[0] + whereToGo[0], position[1] + whereToGo[1]); //x1,y1 are where we currently are, x2,y2 is where we want to go
                position[0] += whereToGo[0];
                position[1] += whereToGo[1];
                sensor.incrementVisited();
            } else {
                //if it is moving back, we just set the location of the machine to the location we popped from the stack
                setBatteryLevel(position[0], position[1], whereToGo[0], whereToGo[1]);
                position[0] = whereToGo[0];
                position[1] = whereToGo[1];
            }
            log.add("Machine is moving to [" + position[0] + "," + position[1] + "]");
            sensor.setVisited(position[0], position[1]);
            log.add("Machine is currently at [" + position[0] + "," + position[1] + "]");
            whereToGoStack[0] = Integer.valueOf(position[0]);
            whereToGoStack[1] = Integer.valueOf(position[1]);
            previousPath.push(whereToGoStack);
            log.add("Coordinates [" + whereToGoStack[0] + ", " + whereToGoStack[1] + "] has been added to the stack");

            getBatteryLevel(); //printout the current BatteryLevel.

            log.add("--------------------checkDirtBegin-------------------");
            checkDirt(position[0], position[1], position[0] - whereToGo[0], position[1] - whereToGo[1]); //x1,y1 is the new square. x2,y2 is the old square
            log.add("--------------------checkDirtEnd---------------------");
            System.out.println();



            // if current BatteryLevel is less than 50, convert to rechargeMode and push the last visited location to the Stack
            if (batteryLevel < 69) {//set value to 69 for testing
                rechargeMode = true;
                //push current location to the rechargePath stack
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                rechargePathStack.push(whereToGoStack);
                //initiate a findPath instance and call findPath function. Passing current location and target location as parameters
                findPath recharge = new findPath();
                List<Cell> rechargePath = recharge.findPath(position[0],position[1],0,0);
                log.add("Found path to charging station:"+ rechargePath);

                //convert List[Cell] to a global Queue for further manipulation
                for(Cell cell:rechargePath){
                    Integer[] eachCell = {cell.i,cell.j};
                    rechargePathQueue.add(eachCell);
                }
            }

            if(dirtLoad >= dirtCapacity){
                emptydirtMode = true;
                //push current location to the rechargePath stack
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                emptydirtPathStack.push(whereToGoStack);

                findPath dirt = new findPath();
                List<Cell> emptydirtPath = dirt.findPath(position[0],position[1],0,0);
                log.add("Dirt capacity has been filled. Found path to charging station:"+ emptydirtPath);
                //convert List[Cell] to a global Queue for further manipulation
                for(Cell cell:emptydirtPath){
                    Integer[] eachCell = {cell.i,cell.j};
                    emptydirtPathQueue.add(eachCell);
                }
            }
            return true;
        }
        else if (rechargeMode) {
            log.add("Recharge mode working.");
            // recharge mode going back to charging station
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            log.add("Machine is currently at [" + position[0] + "," + position[1] + "]");

            // back to charging station
            if (position[0] == 0 & position[1] == 0) {
                log.add("Robot recharging......");
                batteryLevel = 100;
                log.add("Battery fully recharged to 100.");
                rechargeMode = Boolean.FALSE;//change from recharge mode to cleaning mode
                backtoClean(1);//call backtoClean() method, to back to the location that has been last visited in last cleaning mode
            }

            // push the path to charging station to the stack
            else {
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                rechargePathStack.push(whereToGoStack);
                log.add("pushing " + position[0] + ", " + position[1] + " to the stack");
                setBatteryLevel(position[0] - whereToGo[0], position[1] -  whereToGo[1], position[0], position[1]); //x1,y1 is where we currently are. x2,2 is where we were
                getBatteryLevel();
            }
            return true;
        }

        else if(emptydirtMode){
            log.add("Moving to charging station to empty dirt load.");
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            log.add("Machine is currently at [" + position[0] + "," + position[1] + "]");

            // back to charging station
            if (position[0] == 0 & position[1] == 0) {
                batteryLevel = 100;
                log.add("Battery fully recharged to 100.");
                log.add("Empty Me....");
                emptystopFlag = true;
               // System.exit(0);
            }
            // push the path to charging station to the stack
            else {
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                emptydirtPathStack.push(whereToGoStack);
                log.add("Empty Dirt mode move to " + position[0] + ", " + position[1]);
                setBatteryLevel(position[0] - whereToGo[0], position[1] -  whereToGo[1], position[0], position[1]); //x1,y1 is where we currently are. x2,2 is where we were
                getBatteryLevel();
            }
            return true;
        }
        else return false;
    }

    // set battery level for each move
    public void setBatteryLevel(int x1, int y1, int x2, int y2) {
        //floorType need to be implemented here
        if (sensor.getFloorType(x1, y1) == 0 && sensor.getFloorType(x2, y2) == 0) {
            batteryLevel -= 1;
            log.add("Bare floor to Bare Floor");
        } else if (sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 1) {
            batteryLevel -= 2;
            log.add("Low-pile carpet - Low-pile carpet");
        } else if (sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 2) {
            log.add("High-pile carpet - High-pile carpet");
            batteryLevel -= 3;
        } else if (sensor.getFloorType(x1, y1) == 0 && sensor.getFloorType(x2, y2) == 1 || sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 0) {
            log.add("Bare floor - low pile carpet");
            batteryLevel -= 1.5;
        } else if (sensor.getFloorType(x1, y1) == 0 && sensor.getFloorType(x2, y2) == 2 || sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 0) {
            log.add("Bare floor - High pile carpet");
            batteryLevel -= 2;
        } else if (sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 2 || sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 1) {
            log.add("Low pile carpet - High pile carpet");
            batteryLevel -= 2.5;
        }

    }

    public void getBatteryLevel() {
        log.add("The battery level now is " + batteryLevel);
    }


    // moving the robot back to the location that has been last visited before recharging/emptydirt
    public void backtoClean(int mode_num) {
        switch(mode_num) {
            //case 1 is for recharge mode
            case 1: {
                while (!rechargePathStack.empty()) {
                    Integer[] nextLocation = rechargePathStack.pop();
                    int nextLocationX = nextLocation[0];
                    int nextLocationY = nextLocation[1];
                    setBatteryLevel(position[0], position[1], nextLocationX, nextLocationY); //x1,y1 is where we are. nextLocX nextLocY are where we will move to

                    position[0] = nextLocationX;
                    position[1] = nextLocationY;
                    log.add("Back to the last visited location before recharging, machine is currently at [" + position[0] + "," + position[1] + "]");
                    getBatteryLevel();
                }
                break;
            }
            //case 2 is for empty dirt mode
            case 2: {
                while (!emptydirtPathStack.empty()) {
                    Integer[] nextLocation = emptydirtPathStack.pop();
                    int nextLocationX = nextLocation[0];
                    int nextLocationY = nextLocation[1];
                    setBatteryLevel(position[0], position[1], nextLocationX, nextLocationY); //x1,y1 is where we are. nextLocX nextLocY are where we will move to
                    position[0] = nextLocationX;
                    position[1] = nextLocationY;
                    log.add("Back to the last visited location before empty dirt load, machine is currently at [" + position[0] + "," + position[1] + "]");
                    getBatteryLevel();
                }
                break;
            }
        }
    }

    public void checkDirt(int x1, int y1, int x2, int y2) {
        while (sensor.isDirt(x1, y1)) {
            int dirtLevel = sensor.getDirt(x1, y1);
            log.add("dirt detected at [" + x1 + "," + y1 + "], dirt level is " + dirtLevel);

            sensor.setDirt(x1, y1, dirtLevel - 1);
            dirtLoad += 1;
            log.add("clean dirt, current dirt level is " + (dirtLevel - 1));
            log.add("robot dirt load is " + dirtLoad);
            setBatteryLevel(x1, y1, x2, y2); //set BatteryLevel for each vacuum.
            getBatteryLevel(); //printout the current BatteryLevel.
        }
    }

    public void printLog(PrintStream out1, PrintStream out2){
        for (String item: log) {
            output(item,out1,out2);
        }
    }
    //output the logs to out1(write to file) and out2(printout in console) at the same time
    public void output(final String msg, PrintStream out1, PrintStream out2) {
        out1.println(msg);
        out2.println(msg);
}
}
