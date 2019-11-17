package System;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SensorSystemTest {

    @InjectMocks
    private SensorSystem sensorSystem;

    @Mock //setting mock as FloorUnit instead of FloorUnit[][] because Mockito will throw an exception
    private FloorUnit floorUnit;

    @Test(expected = NullPointerException.class)
    public void isDirt_Exception(){
        when(floorUnit.getIsDirt()).thenReturn(true);
        sensorSystem.isDirt(1,1);
    }

    @Test (expected = NullPointerException.class)
    public void isObstacle_Exception(){
        sensorSystem.dimensions = 5;
        sensorSystem.isObstacle(4,4);
    }

    @Test (expected = NullPointerException.class)
    public void isVisited_Exception(){
        sensorSystem.dimensions = 5;
        sensorSystem.isVisited(4,4);
    }

    @Test (expected = NullPointerException.class)
    public void setDirt_Exception(){
        sensorSystem.setDirt(1,1,0);
    }

    @Test (expected = NullPointerException.class)
    public void setVisited_Exception(){
        sensorSystem.setVisited(1,1);
    }

    @Test (expected = NullPointerException.class)
    public void getDirt_Exception(){
        sensorSystem.getDirt(1,1);
    }

    @Test (expected = NullPointerException.class)
    public void getFloorType_Exception(){
        sensorSystem.getFloorType(1,1);
    }

    @Test
    public void getTotalTiles(){
        sensorSystem.numTiles = 100;
        Assert.assertEquals(100, sensorSystem.getTotalTiles());
    }

    @Test
    public void incrementVisited(){
        sensorSystem.numVisited = 0;
        sensorSystem.incrementVisited();
        Assert.assertEquals(1,sensorSystem.getVisitedTiles());
    }

    @Test
    public void getVisitedTiles(){
        sensorSystem.numVisited = 1;
        Assert.assertEquals(1,sensorSystem.getVisitedTiles());
    }
}
