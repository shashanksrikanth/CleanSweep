public ControlSystem{

	enum Direction{
		UP, DOWN, LEFT, RIGHT
	}



	Direction currDirection;

	
	
	//Create an object of DeviceLocation to keep track of the device's location
	DeviceLocation currentLocation = DeviceLocation.getInstance();


	//Create a sensor system variable with a floorPlan
	SensorSystem sensorSystem = new SensorSystem();
	int[][] floorPlan = sensorSystem.read();

	//variable that tells the robot where to go next
	int [] destination;


	//Constructor
	public ControlSystem(int[][] floorPlan){
		this.floorPlan = floorPlan;
	}

	//function that operates the device to be called in main.
	operateDevice(){
		//the base case is if there's nowhere else to move. I.e. if nextLocation == current location then we retunr and stop the execution of operateDevice().
		//else do below
		int [] nextLocation = checkWhereToMove();
		pickUpDirt();
		moveDevice();
		operateDevice();
		return;
	}



	moveDevice(){
		//currentLocation.moveUp or down or left or right depending on what the enum variable is set to. 
	}





	//function that checks all the adjacent squares and return where to move
	int[] checkWhereToMove(){
		//look at the current location and increase the x variable by one. see if it's an obstacle or if it's out of bounds
			//if it is, then return the new destination and set the Direction variable to RIGHT 
		//else, look at x - 1 location and repeat. 
		//repeat for y updates until the function returns. 

	}

	pickUpDirt(){
		//if there's dirt then pick it up
		//else move along
	}



}

//Singleton class which looks at the device's location.
DeviceLocation{
	private int x,y;
	private int[] position = new int[2];
	position = {x,y};

	private static DeviceLocation singleInstance;

	public static getInstance(){
		if(singleInstance == null){
			singleInstance = new Mode();
		}
		return singleInstance;
	}

	//pass as x and y the location where the device starts on the floor plan. 
	//Assuming it's (0,0) for the time being. To keep things simple. 
	DeviceLocation(){
		this.x = 0;
		this.y = 0;

	}

	//This function could break the program!! 
	//We should figure out a different way to set the initial location. RETHINK THIS!
	setLocation(int x, int y){
		position[0] = x;
		position[1] = y;
	}

	int[] getLocation(){
		return this.position;
	}

	//increase the y variable
	moveUp(){
		position[1] += 1;
	}

	//decrease the y variable
	moveDown(){
		position[1] -= 1;

	}

	//decrease the x variable
	moveLeft(){
		position[0] -= 1;
	}


	//increase the x variable
	moveRight(){
		position[0] += 1;
	}


}

