package System;

import java.io.*;

public class SensorSystem {

    FloorUnit[][] floorPlan; //2D array of FloorUnit object
    int dimensions; //dimensions of the floor plan
    int numTiles;
    int numVisited = 0;

    public boolean isDirt(int row, int col) {
        return floorPlan[row][col].getIsDirt();
    }

    //getter method for determining if there is an obstacle in a unit
    public boolean isObstacle(int row, int col) {
        if (row < dimensions && col < dimensions && row >= 0 && col >= 0)
            return floorPlan[row][col].getIsObstacle();
        else return true;
    }

    public boolean isVisited(int row, int col) {
        if (row < dimensions && col < dimensions)
            return floorPlan[row][col].getIsVisited();
        else return true;
    }

    //once the control system cleans a unit, we want to update the floor plan saying that the unit is no longer dirty.
    public void setDirt(int row, int col, int dirtLevel) {
        floorPlan[row][col].setDirtLevel(dirtLevel);
    }

    //want to mark if the unit has been visited already
    public void setVisited(int row, int col) {
        floorPlan[row][col].visited = 1;
    }

    //reading in a floor system file
    public void readFile(String fileName) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            dimensions = Integer.parseInt(reader.readLine());
            floorPlan = new FloorUnit[dimensions][dimensions];
            String line = reader.readLine();
            while (line != null) {
                String[] information = line.split(",");
                int rowNum = Integer.parseInt(information[0]);
                int colNum = Integer.parseInt(information[1]);
                int dirtLevel = Integer.parseInt(information[2]);
                int obstacle = Integer.parseInt(information[3]);
                if (obstacle == 0) numTiles++;
                int visited = Integer.parseInt(information[4]);
                int floorType = Integer.parseInt(information[5]); //khoa lines add to floor type.
                floorPlan[rowNum][colNum] = new FloorUnit(dirtLevel, obstacle, visited, floorType); //khoa added floorType
                line = reader.readLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //add feature to get dirt level for a location
    public int getDirt(int row, int col) {
        return floorPlan[row][col].getDirtLevel();
    }

    //return floor type
    public int getFloorType(int row, int col) {
        return floorPlan[row][col].getFloorType();
    }

    //get numTiles
    public int getTotalTiles() {
        return numTiles;
    }

    //increment number of tiles visited by the vacuum cleaner
    public void incrementVisited() {
        numVisited++;
    }

    //get numVisited
    public int getVisitedTiles() {
        return numVisited;
    }
}

