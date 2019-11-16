package System;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import System.findPath.Cell;

public class ControlSystem {

    //instance variables
    static SensorSystem sensor;
    float batteryLevel = 100;
    int[] position = new int[2];
    boolean rechargeMode = false;
    boolean emptydirtMode = false;
    Stack<Integer[]> rechargePathStack = new Stack<>();//stack used for moving back from charge station to the last visited tile
    Queue<Integer[]> rechargePathQueue = new LinkedList<>();//queue used for moving to charge station
    Queue<Integer[]> emptydirtPathQueue = new LinkedList<>();//queue used for moving to charge station
    Stack<Integer[]> emptydirtPathStack = new Stack<>();//stack used for moving back from charge station
    Stack<Integer[]> previousPath = new Stack<>(); //Stack for basic movement (obstacle recognition)

    boolean moveBack = false; //flag for whether the machine is moving back or not
    int dirtCapacity = 10;
    int dirtLoad = 0;

    public ControlSystem(SensorSystem sensor) {
        this.sensor = sensor;
        position[0] = 0;
        position[1] = 0;
    }

    public int[] checkWhereToMove() {
        //where next to move
        int[] coordinates = new int[2];

        //cleaning mode moving algorithm
        if (!rechargeMode & !emptydirtMode) {
            //check if anything is down (per our POV)
            if (!sensor.isObstacle(position[0], position[1] + 1) && !sensor.isVisited(position[0], position[1] + 1)) {
                System.out.println("Checking " + position[0] + ", " + (position[1] + 1));
                coordinates[0] = 0;
                coordinates[1] = 1;
                moveBack = false;
            }
            //check if anything is in the right (per our POV)
            else if (!sensor.isObstacle(position[0] + 1, position[1]) && !sensor.isVisited(position[0] + 1, position[1])) {
                System.out.println("Checking " + (position[0] + 1) + ", " + position[1]);
                coordinates[0] = 1;
                coordinates[1] = 0;
                moveBack = false;
            }
            //check if anything is on the left (per our POV)
            else if (!sensor.isObstacle(position[0] - 1, position[1]) && !sensor.isVisited(position[0] - 1, position[1])) {
                System.out.println("Checking " + (position[0] - 1) + ", " + position[1]);
                coordinates[0] = -1;
                coordinates[1] = 0;
                moveBack = false;
            } else if (!sensor.isObstacle(position[0], position[1] - 1) && !sensor.isVisited(position[0], position[1] - 1)) {
                System.out.println("Checking " + (position[0]) + ", " + (position[1] - 1));
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


    public boolean moveDevice() {
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
            System.out.println("Machine is moving to [" + position[0] + "," + position[1] + "]");
            sensor.setVisited(position[0], position[1]);
            System.out.println("Machine is currently at [" + position[0] + "," + position[1] + "]");
            whereToGoStack[0] = Integer.valueOf(position[0]);
            whereToGoStack[1] = Integer.valueOf(position[1]);
            previousPath.push(whereToGoStack);
            System.out.println("Coordinates [" + whereToGoStack[0] + ", " + whereToGoStack[1] + "] has been added to the stack");

            getBatteryLevel(); //printout the current BatteryLevel.

            System.out.println("--------------------checkDirtBegin-------------------");
            checkDirt(position[0], position[1], position[0] - whereToGo[0], position[1] - whereToGo[1]); //x1,y1 is the new square. x2,y2 is the old square
            System.out.println("--------------------checkDirtEnd---------------------");
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
                System.out.println("Found path to charging station:"+ rechargePath);

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
                System.out.println("Dirt capacity has been filled. Found path to charging station:"+ emptydirtPath);
                //convert List[Cell] to a global Queue for further manipulation
                for(Cell cell:emptydirtPath){
                    Integer[] eachCell = {cell.i,cell.j};
                    emptydirtPathQueue.add(eachCell);
                }
            }
            return true;
        }
        else if (rechargeMode) {
            System.out.println("Recharge mode working.");
            // recharge mode going back to charging station
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            System.out.println("Machine is currently at [" + position[0] + "," + position[1] + "]");

            // back to charging station
            if (position[0] == 0 & position[1] == 0) {
                System.out.println("Robot recharging......");
                batteryLevel = 100;
                System.out.println("Battery fully recharged to 100.");
                System.out.println();
                rechargeMode = Boolean.FALSE;//change from recharge mode to cleaning mode
                backtoClean(1);//call backtoClean() method, to back to the location that has been last visited in last cleaning mode
            }

            // push the path to charging station to the stack
            else {
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                rechargePathStack.push(whereToGoStack);
                System.out.println("pushing " + position[0] + ", " + position[1] + " to the stack");
                setBatteryLevel(position[0] - whereToGo[0], position[1] -  whereToGo[1], position[0], position[1]); //x1,y1 is where we currently are. x2,2 is where we were
                getBatteryLevel();
                System.out.println();
            }
            return true;
        }

        else if(emptydirtMode){
            System.out.println("Moving to charging station to empty dirt load.");
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            System.out.println("Machine is currently at [" + position[0] + "," + position[1] + "]");

            // back to charging station
            if (position[0] == 0 & position[1] == 0) {
                batteryLevel = 100;
                System.out.println("Battery fully recharged to 100.");
                System.out.println("Empty Me....");

//                System.out.println("Press Enter to empty the dirt.");
//                try{
//                    System.in.read();
//                    dirtLoad = 0;
//                    backtoClean(2);
//                    emptydirtMode = false;
//                }
//                catch(Exception e){}

        }
            // push the path to charging station to the stack
            else {
                whereToGoStack[0] = Integer.valueOf(position[0]);
                whereToGoStack[1] = Integer.valueOf(position[1]);
                emptydirtPathStack.push(whereToGoStack);
                System.out.println("Empty Dirt mode move to " + position[0] + ", " + position[1]);
                setBatteryLevel(position[0] - whereToGo[0], position[1] -  whereToGo[1], position[0], position[1]); //x1,y1 is where we currently are. x2,2 is where we were
                getBatteryLevel();
                System.out.println();
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
            System.out.println("Bare floor to Bare Floor");
        } else if (sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 1) {
            batteryLevel -= 2;
            System.out.println("Low-pile carpet - Low-pile carpet");
        } else if (sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 2) {
            System.out.println("High-pile carpet - High-pile carpet");
            batteryLevel -= 3;
        } else if (sensor.getFloorType(x1, y1) == 0 && sensor.getFloorType(x2, y2) == 1 || sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 0) {
            System.out.println("Bare floor - low pile carpet");
            batteryLevel -= 1.5;
        } else if (sensor.getFloorType(x1, y1) == 0 && sensor.getFloorType(x2, y2) == 2 || sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 0) {
            System.out.println("Bare floor - High pile carpet");
            batteryLevel -= 2;
        } else if (sensor.getFloorType(x1, y1) == 1 && sensor.getFloorType(x2, y2) == 2 || sensor.getFloorType(x1, y1) == 2 && sensor.getFloorType(x2, y2) == 1) {
            System.out.println("Low pile carpet - High pile carpet");
            batteryLevel -= 2.5;
        }

    }

    public void getBatteryLevel() {
        System.out.println("The battery level now is " + batteryLevel);
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
                    System.out.println("Back to the last visited location before recharging, machine is currently at [" + position[0] + "," + position[1] + "]");
                    getBatteryLevel();
                    System.out.println();
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
                    System.out.println("Back to the last visited location before empty dirt load, machine is currently at [" + position[0] + "," + position[1] + "]");
                    getBatteryLevel();
                    System.out.println();
                }
                break;
            }
        }
    }

    public void checkDirt(int x1, int y1, int x2, int y2) {
        while (sensor.isDirt(x1, y1)) {
            int dirtLevel = sensor.getDirt(x1, y1);
            System.out.println("dirt detected at [" + x1 + "," + y1 + "], dirt level is " + dirtLevel);

            sensor.setDirt(x1, y1, dirtLevel - 1);
            dirtLoad += 1;
            System.out.println("clean dirt, current dirt level is " + (dirtLevel - 1));
            System.out.println("robot dirt load is " + dirtLoad);
            setBatteryLevel(x1, y1, x2, y2); //set BatteryLevel for each vacuum.
            getBatteryLevel(); //printout the current BatteryLevel.
        }
    }

}




