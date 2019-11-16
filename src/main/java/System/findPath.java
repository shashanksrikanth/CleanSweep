package System;

import java.util.LinkedList;
import java.util.List;

// implement A* path finding algorithm, find the shortest path from one location to another with obstacles in the map
// https://www.raywenderlich.com/3016-introduction-to-a-pathfinding

public class findPath {

    //initiate each tile to Cell, that has several attributes for A* path finding algorithm
    static class Cell {
        int Gcosts = 0;
        int Hcosts = 0;
        int i, j;
        boolean walkable;
        Cell parent;

        Cell(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void setGcosts(Cell parentCell, int basicCost) {
            Gcosts = parentCell.getGcosts() + basicCost;
        }

        public int calculateGcosts(Cell parentCell, int movementCost) {
            return (parentCell.getGcosts() + movementCost);
        }

        public int getGcosts() {
            return Gcosts;
        }

        public void setHcosts(Cell endCell) {
            Hcosts = Math.abs(i-endCell.i) + Math.abs(j-endCell.j);
        }

        public int getFcosts() {
            return Gcosts + Hcosts;
        }
        public void setParent(Cell parent) {
            this.parent = parent;
        }
        public Cell getParent(){
            return parent;
        }
        public boolean isWalkable() {
            return walkable;
        }

        public void setWalkable(boolean walkable) {
            this.walkable = walkable;
        }

        @Override
        public String toString() {
            return "[" + this.i + ", " + this.j + "]";
        }
    }

    //use the sensor from ControlSystem to initialize several global variables
    int height = ControlSystem.sensor.dimensions -1;
    int width = ControlSystem.sensor.dimensions -1;
    Cell[][] grid = new Cell[width+1][height+1];

    /** list containing nodes not visited but adjacent to visited nodes. */
    private List<Cell> openList;
    /** list containing nodes already visited/taken care of. */
    private List<Cell> closedList;

    private boolean done = false;

    // main method, take start point and end point as parameter, and return the list of Cell that makes the shortest path from start to end
    public List<Cell> findPath(int startX, int startY, int endX, int endY) {
        openList = new LinkedList<Cell>();
        closedList = new LinkedList<Cell>();

        //initializes all nodes. Their coordinates will be set correctly.
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                grid[i][j] = new Cell(i, j);
                //set obstacles on the map, use ControlSystem.sensor to read the map
                setWalkable(i, j, !ControlSystem.sensor.isObstacle(i, j));
            }
        }

        openList.add(grid[startX][startY]); // add starting point to open list
        done = false;
        Cell current;

        while (!done) {
            current = lowestFInOpen(); // get node with lowest fCosts from openList
            closedList.add(current); // add current node to closed list
            openList.remove(current); // delete current node from open list

            if ((current.i == endX) && (current.j == endY)) { // found goal
                return calcPath(grid[startX][startY], current);
            }

            // for all adjacent cells:
            List<Cell> adjacentCells = getAdjacent(current);
            for (int i = 0; i < adjacentCells.size(); i++) {
                Cell currentAdj = adjacentCells.get(i);
                if (!openList.contains(currentAdj)) { // node is not in openList
                    currentAdj.setParent(current); // set current node as previous for this node
                    currentAdj.setHcosts(grid[endX][endY]); // set h costs of this node (estimated costs to goal)
                    currentAdj.setHcosts(current); // set g costs of this node (costs from start to this node)
                    openList.add(currentAdj); // add node to openList
                } else { // node is in openList
                    if (currentAdj.getGcosts() > currentAdj.calculateGcosts(current,1)) { // costs from current node are cheaper than previous costs
                        currentAdj.setParent(current); // set current node as previous for this node
                        currentAdj.setGcosts(current,1); // set g costs of this node (costs from start to this node)
                    }
                }
            }

            if (openList.isEmpty()) { // no path exists
                return new LinkedList<Cell>(); // return empty list
            }
        }
        return null; // unreachable
    }

    //this method return the cell that has lowest F cost from openList
    private Cell lowestFInOpen() {
        Cell cheapest = openList.get(0);

        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getFcosts() < cheapest.getFcosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private List<Cell> calcPath(Cell start, Cell goal) {
        LinkedList<Cell> path = new LinkedList<Cell>();

        Cell curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = (Cell) curr.getParent();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }

    //set obstacles and walkable nodes in the map
    public void setWalkable(int x, int y, boolean bool) {
        grid[x][y].setWalkable(bool);
    }


    private List<Cell> getAdjacent(Cell node) {
        int x = node.i;
        int y = node.j;
        List<Cell> adj = new LinkedList<Cell>();
        Cell temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }

        if (x < width) {
            temp = this.getNode((x + 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }

        if (y < height) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                adj.add(temp);
            }
        }
        return adj;
    }

    public final Cell getNode(int x, int y) {
        return grid[x][y];
    }

}