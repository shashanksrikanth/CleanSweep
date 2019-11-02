package System;

import java.util.Stack;

public class ControlSystem {

    //instance variables
    SensorSystem sensor;
    int batteryLevel = 100;
    int[] position = new int[2];
    Boolean rechargeMode = false;
    // Stack<int[]> rechargePath = new Stack<>();
    Stack<Integer> rechargePathX = new Stack<>();
    Stack<Integer> rechargePathY = new Stack<>();

    public ControlSystem(SensorSystem sensor){
        this.sensor = sensor;

        position[0]= 0;
        position[1]=0;
    }


    public int[] checkWhereToMove(){
        //where next to move
        int[]coordinates = new int[2];

        //cleaning mode moving algorithm
        if(!rechargeMode) {
            //check if anything is down (per our POV)
            if (!sensor.isObstacle(position[0], position[1] + 1) && !sensor.isVisited(position[0], position[1] + 1)) {
                System.out.println("Checking " + position[0] + ", " + (position[1] + 1));
                coordinates[0] = 0;
                coordinates[1] = 1;
            }
            //check if anything is in the right (per our POV)
            else if (!sensor.isObstacle(position[0] + 1, position[1]) && !sensor.isVisited(position[0] + 1, position[1])) {
                System.out.println("Checking " + (position[0] + 1) + ", " + position[1]);
                coordinates[0] = 1;
                coordinates[1] = 0;
            }
            //check if anything is on the left (per our POV)
            else if (!sensor.isObstacle(position[0] - 1, position[1]) && !sensor.isVisited(position[0] - 1, position[1])) {
                System.out.println("Checking " + (position[0] - 1) + ", " + position[1]);
                coordinates[0] = -1;
                coordinates[1] = 0;
            } else if (!sensor.isObstacle(position[0], position[1] - 1) && !sensor.isVisited(position[0], position[1] - 1)) {
                System.out.println("Checking " + (position[0]) + ", " + (position[1] - 1));
                coordinates[0] = 0;
                coordinates[1] = -1;
            } else {
                System.out.println("Full floor has been traversed");
                coordinates[0] = Integer.MAX_VALUE;
                coordinates[1] = Integer.MAX_VALUE;
            }
            return coordinates;
        }

        //moving algorithm for recharge mode, moving back to the charging station(0,0)
        else {
            //check if anything is on the upper
            if (!sensor.isObstacle(position[0], position[1] - 1)) {
                System.out.println("Recharge mode move to " + (position[0]) + ", " + (position[1] - 1));
                coordinates[0] = 0;
                coordinates[1] = -1;
            }
            //check if anything is on the left (per our POV)
            else if (!sensor.isObstacle(position[0] - 1, position[1])) {
                System.out.println("Recharge mode move to " + (position[0] - 1) + ", " + position[1]);
                coordinates[0] = -1;
                coordinates[1] = 0;
            }
            //check if anything is in the right (per our POV)
            else if (!sensor.isObstacle(position[0] + 1, position[1])) {
                System.out.println("Recharge mode move to " + (position[0] + 1) + ", " + position[1]);
                coordinates[0] = 1;
                coordinates[1] = 0;
            }
            // anything is down
            else if (!sensor.isObstacle(position[0], position[1] + 1)) {
                System.out.println("Recharge mode move to " + position[0] + ", " + (position[1] + 1));
                coordinates[0] = 0;
                coordinates[1] = 1;
            }

            return coordinates;
        }
    }


    public boolean moveDevice(){
        int[]whereToGo = this.checkWhereToMove();
        //if it has not traverse the whole floor and not in recharge mode, it means it is in cleaning mode
        if(whereToGo[0]!=Integer.MAX_VALUE & !rechargeMode) {
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            sensor.setVisited(position[0], position[1]);
            System.out.println("Machine is currently at [" + position[0] + "," + position[1] + "]");
            setBatteryLevel(); //set BatteryLevel for each move
            getBatteryLevel(); //printout the current BatteryLevel.

            System.out.println("--------------------checkDirtBegin-------------------");
            checkDirt(position[0],position[1]);
            System.out.println("--------------------checkDirtEnd---------------------");
            System.out.println();

            // if current BatteryLevel is less than 50, convert to rechargeMode and push the last visited location to the Stack
            if(batteryLevel < 50) {
                rechargeMode = Boolean.TRUE;
                rechargePathX.push(position[0]);
                rechargePathY.push(position[1]);
                System.out.println("pushing " + position[0] +", "+ position[1] + " to the stack");
            }
            return true;
        }


        else if(rechargeMode){
            System.out.println("Recharge mode working.");
            // recharge mode going back to charging station
            position[0] += whereToGo[0];
            position[1] += whereToGo[1];
            System.out.println("Machine is currently at [" + position[0] + "," + position[1] + "]");

            // back to charging station
            if(position[0] == 0 & position[1] == 0) {
                System.out.println("Robot recharging......");
                batteryLevel = 100;
                System.out.println("Battery fully recharged to 100.");
                System.out.println();
                rechargeMode = Boolean.FALSE;//change from recharge mode to cleaning mode
                backtoClean();//call backtoClean() method, to back to the location that has been last visited in last cleaning mode
            }

            // push the path to charging station to the stack
            else{
                // rechargePath.push(position);
                rechargePathX.push(position[0]);
                rechargePathY.push(position[1]);
                System.out.println("pushing " + position[0] +", "+ position[1] + " to the stack");
                setBatteryLevel();
                getBatteryLevel();
                System.out.println();
            }
            return true;
        }

        else return false;
    }

    // set battery level for each move
    public void setBatteryLevel(){
        //floorType need to be implemented here
        batteryLevel -= 5;
    }

    public void getBatteryLevel(){
        System.out.println("The battery level now is "+ batteryLevel);
    }

    // moving the robot back to the location that has been last visited before recharging
    public void backtoClean(){
        while (!rechargePathX.empty()){
            // int[] nextLocation = rechargePath.pop();
            int nextLocationX = rechargePathX.pop();
            int nextLocationY = rechargePathY.pop();

            position[0] = nextLocationX;
            position[1] = nextLocationY;
            System.out.println("Back to the last visited location before recharging, machine is currently at [" + position[0] + "," + position[1] + "]");
            setBatteryLevel();
            getBatteryLevel();
            System.out.println();
        }
    }

    public void checkDirt(int x,int y){
        while(sensor.isDirt(x,y)){
            int dirtLevel = sensor.getDirt(x,y);
            System.out.println("dirt detected at ["+x + "," +y+"], dirt level is "+ dirtLevel);

            sensor.setDirt(x,y,dirtLevel-1);
            System.out.println("clean dirt, current dirt level is "+ (dirtLevel - 1));
            setBatteryLevel(); //set BatteryLevel for each vacuum. need floor type
            getBatteryLevel(); //printout the current BatteryLevel.
        }
    }

}
