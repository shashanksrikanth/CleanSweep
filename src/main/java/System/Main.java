package System;

import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException {
        SensorSystem sensor = new SensorSystem();
        sensor.readFile("src/main/java/System/floorplan.txt");
        ControlSystem control = new ControlSystem(sensor);
        while(control.moveDevice()) control.moveDevice();
    }
}
