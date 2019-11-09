//package System;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//public class FloorUnitTest {
//
//    @Test
//    public void getIsDirt_True(){
//        FloorUnit floorUnit = new FloorUnit(5,0 );
//        Assert.assertEquals(true, floorUnit.getIsDirt());
//    }
//
//    @Test
//    public void getIsDirt_False(){
//        FloorUnit floorUnit = new FloorUnit(0,0);
//        Assert.assertEquals(false, floorUnit.getIsDirt());
//    }
//
//    @Test
//    public void getIsObstacle_True(){
//        FloorUnit floorUnit = new FloorUnit(0,1);
//        Assert.assertEquals(true, floorUnit.getIsObstacle());
//    }
//
//    @Test
//    public void getIsObstacle_False(){
//        FloorUnit floorUnit = new FloorUnit(0,0);
//        Assert.assertEquals(false, floorUnit.getIsObstacle());
//    }
//
//    @Test
//    public void setDirtLevel(){
//        FloorUnit floorUnit = new FloorUnit(0, 0);
//        Assert.assertEquals(false, floorUnit.getIsDirt());
//        floorUnit.setDirtLevel(1);
//        Assert.assertEquals(true, floorUnit.getIsDirt());
//    }
//
//}
