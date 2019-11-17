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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControlSystemTest {

    @InjectMocks
    private ControlSystem controlSystem;

    @Mock
    private SensorSystem sensorSystem;

    @Mock
    private FloorUnit floorUnit;

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
}
