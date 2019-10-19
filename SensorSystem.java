public class SensorSystem{
    
    //getter method for determining if there is dirt in a unit
    public boolean isDirt(int row, int col, String direction){
        //code
        return true;
    }

    //getter method for determining if there is an obstacle in a unit
    public boolean isObstacle(int row, int col, String direction){
        //code
        return true;
    }

    //once the control system cleans a unit, we want to update the floor plan saying that the unit is no longer dirty.
    public void setDirt (int row, int col, int dirtLevel){
        //code
    }

    //reading in a floor system file
    public void readFile (File file){
        //code
    }
}