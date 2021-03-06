package System;

public class FloorUnit {
        //instance variables
        int dirtLevel;
        int obstacle;
        int visited;
        int floorType;

        //constructor for FloorUnit
        public FloorUnit(int dirtLevel, int obstacle, int visited, int floorType){ //added floorType
            this.dirtLevel = dirtLevel;
            this.obstacle = obstacle;
            this.visited = visited;
            this.floorType=floorType;
        }

        public void setDirtLevel (int newDirt) {
            this.dirtLevel = newDirt;}

        public boolean getIsDirt() {
            if(this.dirtLevel>0) return true;
            return false;
        }
        public boolean getIsObstacle() {
            if(this.obstacle==1) return true;
            return false;
        }
        public boolean getIsVisited(){
            if(this.visited==1) return true;
            return false;
        }
        //add feature to get dirt level from the floor unit
        public int getDirtLevel(){
            return dirtLevel;
        }

        public int getFloorType(){
            return floorType;
        }
}
