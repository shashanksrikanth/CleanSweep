package System;

public class FloorUnit {
        //instance variables
        int dirtLevel;
        int obstacle;

        //constructor for FloorUnit
        public FloorUnit(int dirtLevel, int obstacle){
            this.dirtLevel = dirtLevel;
            this.obstacle = obstacle;
        }

        public void setDirtLevel (int newDirt) {this.dirtLevel = newDirt;}
        public boolean getIsDirt() {
            if(this.dirtLevel>0) return true;
            return false;
        }
        public boolean getIsObstacle() {
            if(this.obstacle==1) return true;
            return false;
        }
}
