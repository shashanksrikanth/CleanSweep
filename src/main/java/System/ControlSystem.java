package System;

public class ControlSystem {

    //instance variables
    SensorSystem sensor;
    int batteryLevel;
    int[]position;

    public ControlSystem(SensorSystem sensor){
        this.sensor = sensor;
        batteryLevel = 100;
        position = new int[2];
        position[0]= 0;
        position[1]=0;
    }

    public void moveDevice (int x, int y){
        position[0]=x;
        position[1]=y;
    }

    public int[] checkWhereToMove(){
        //where next to move
        int[]coordinates = new int[2];
        //check if anything is down (per our POV)
        if(!sensor.isObstacle(position[0], position[1]+1) && !sensor.isVisited(position[0], position[1]+1)) {
            System.out.println("Checking "+position[0]+", "+(position[1]+1));
            coordinates[0]=0;
            coordinates[1]=1;
        }
        //check if anything is in the right (per our POV)
        else if(!sensor.isObstacle(position[0]+1, position[1]) && !sensor.isVisited(position[0]+1, position[1])){
            System.out.println("Checking "+(position[0]+1)+", "+position[1]);
            coordinates[0]=1;
            coordinates[1]=0;
        }
        //check if anything is on the left (per our POV)
        else if(!sensor.isObstacle(position[0]-1, position[1]) && !sensor.isVisited(position[0]-1, position[1])){
            System.out.println("Checking "+(position[0]-1)+", "+position[1]);
            coordinates[0]=-1;
            coordinates[1]=0;
        }
        else{
            coordinates[0]=0;
            coordinates[1]=-1;
        }
        return coordinates;
    }

    public void moveDevice(){
        int[]whereToGo = this.checkWhereToMove();
        position[0]+=whereToGo[0];
        position[1]+=whereToGo[1];
        sensor.setVisited(position[0],position[1]);
        System.out.println("Machine is currently at ["+position[0]+","+position[1]+"]");
    }
}
