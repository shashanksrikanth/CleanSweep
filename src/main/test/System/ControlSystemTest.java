package System;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.naming.ldap.Control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControlSystemTest {

    @InjectMocks
    private ControlSystem controlSystem;

    @Mock
    private SensorSystem sensorSystem;

    @Mock
    private FloorUnit floorUnit;

    @Mock
    private Stack<Integer[]> previousPath;

    @Mock
    private ArrayList<String> log;

    @Test
    public void setBatteryLevel_BareFloorToBareFloor(){

        when(sensorSystem.getFloorType(Mockito.anyInt(),Mockito.anyInt())).thenReturn(0);
        when(floorUnit.getFloorType()).thenReturn(0);
        controlSystem.setBatteryLevel(0,0,0,0);
    }

    @Test
    public void setBatteryLevel_LowPileToLowPile(){
        when(sensorSystem.getFloorType(Mockito.anyInt(),Mockito.anyInt())).thenReturn(1);
        when(floorUnit.getFloorType()).thenReturn(0);
        controlSystem.setBatteryLevel(0,0,0,0);
    }

    @Test
    public void setBatteryLevel_HighPileToHighPIle() {
        when(sensorSystem.getFloorType(Mockito.anyInt(),Mockito.anyInt())).thenReturn(2);
        when(floorUnit.getFloorType()).thenReturn(0);
        controlSystem.setBatteryLevel(0,0,0,0);
    }

    @Test
    public void setBatteryLevel_BareFloorToLowPile() {
        when(sensorSystem.getFloorType(0,0)).thenReturn(0);
        when(sensorSystem.getFloorType(1,1)).thenReturn(1);
        controlSystem.setBatteryLevel(0,0,1,1);
    }

    @Test
    public void setBatteryLevel_BareFloorToHighPile() {
        when(sensorSystem.getFloorType(0,0)).thenReturn(0);
        when(sensorSystem.getFloorType(2,2)).thenReturn(2);
        controlSystem.setBatteryLevel(0,0,2,2);
    }

    @Test
    public void setBatteryLevel_LowPileToHighPile()  {
        when(sensorSystem.getFloorType(1,1)).thenReturn(1);
        when(sensorSystem.getFloorType(2,2)).thenReturn(2);
        controlSystem.setBatteryLevel(1,1,2,2);
    }

    @Test
    public void checkWhereToMove_downIsAvailable(){
        controlSystem.rechargeMode = false;
        controlSystem.emptydirtMode = false;
        when(sensorSystem.isObstacle(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        when(sensorSystem.isVisited(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
        controlSystem.checkWhereToMove();
    }

    @Test
    public void checkDirt_NotDirt(){
       when(sensorSystem.isDirt(1,1)).thenReturn(false);
        controlSystem.checkDirt(1,1,1,1);
    }

    @Test
    public void checkDirt_IsDirt(){
        controlSystem.batteryLevel = 100;
        controlSystem.dirtLoad = 0;
        when(sensorSystem.isDirt(1,1)).thenReturn(true).thenReturn(false);
        when(sensorSystem.getDirt(1,1)).thenReturn(1);
        when(log.add(Mockito.anyString())).thenReturn(true);
        controlSystem.checkDirt(1,1,1,1);

    }

    @Test
    public void printLog(){
        log.add("Item");
//        when(log.get(Mockito.anyInt())).thenReturn("Item Mock");
        controlSystem.printLog();
    }
}
