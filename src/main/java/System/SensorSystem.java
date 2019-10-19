package System;
import java.io.*;

public class SensorSystem {

    FloorUnit[][]floorPlan;
    public boolean isDirt(int row, int col){
        return floorPlan[row][col].getIsDirt();
    }

    //getter method for determining if there is an obstacle in a unit
    public boolean isObstacle(int row, int col){
        return floorPlan[row][col].getIsObstacle();
    }

    //once the control system cleans a unit, we want to update the floor plan saying that the unit is no longer dirty.
    public void setDirt (int row, int col, int dirtLevel){
        floorPlan[row][col].setDirtLevel(dirtLevel);
    }

    //reading in a floor system file
    public void readFile (String fileName) throws IOException{
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int dimensions = Integer.parseInt(reader.readLine());
            floorPlan = new FloorUnit[dimensions][dimensions];
            String line = reader.readLine();
            while(line!=null) {
                String[]information = line.split(",");
                int rowNum = Integer.parseInt(information[0]);
                int colNum = Integer.parseInt(information[1]);
                int dirtLevel = Integer.parseInt(information[2]);
                int obstacle = Integer.parseInt(information[3]);
                floorPlan[rowNum][colNum] = new FloorUnit(dirtLevel, obstacle);
                line = reader.readLine();
            }
            System.out.println("Floor plan recognized!");
            reader.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

