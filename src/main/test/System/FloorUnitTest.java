package System;

import org.junit.Assert;
import org.junit.Test;

public class FloorUnitTest {

    @Test
    public void getIsDirt_True(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 0, 0);
        Assert.assertEquals(true, floorUnit.getIsDirt());
    }

    @Test
    public void getIsDirt_False(){
        FloorUnit floorUnit = new FloorUnit(0,0 , 0, 0);
        Assert.assertEquals(false, floorUnit.getIsDirt());
    }

    @Test
    public void getIsObstacle_True(){
        FloorUnit floorUnit = new FloorUnit(5,1 , 0, 0);
        Assert.assertEquals(true, floorUnit.getIsObstacle());
    }

    @Test
    public void getIsObstacle_False(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 0, 0);
        Assert.assertEquals(false, floorUnit.getIsObstacle());
    }

    @Test
    public void getIsVisited_True(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 1, 0);
        Assert.assertEquals(true, floorUnit.getIsVisited());

    }

    @Test
    public void getIsVisited_False(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 0, 0);
        Assert.assertEquals(false, floorUnit.getIsVisited());
    }

    @Test
    public void setDirtLevel(){
        FloorUnit floorUnit = new FloorUnit(0,0 , 0, 0);
        Assert.assertEquals(false, floorUnit.getIsDirt());
        floorUnit.setDirtLevel(1);
        Assert.assertEquals(true, floorUnit.getIsDirt());
    }

    @Test
    public void getDirtLevel(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 0, 0);
        Assert.assertEquals(5, floorUnit.getDirtLevel());
    }

    @Test
    public void getFloorType(){
        FloorUnit floorUnit = new FloorUnit(5,0 , 0, 0);
        Assert.assertEquals(0, floorUnit.getFloorType());

    }

}
