import java.awt.Point;
import java.util.ArrayList;



public class Person {
	private Maze m_Maze;

	private int m_X = 0;
	private int m_Y = 0;
	private Orientation m_Orientation = Orientation.NORTH;
	private int m_Rotations = 0;// clockwise => +
	
	private ArrayList<Point> m_WayOut;

	public Person(Maze maze, int x, int y) {
		m_Maze = maze;
		m_X = x;
		m_Y = y;
		m_WayOut = new ArrayList<Point>();
		m_Rotations = 0;
		m_Orientation = Orientation.NORTH;
	}

	public void setPosition(int x, int y) {
		m_X = x;
		m_Y = y;
		
		m_WayOut = new ArrayList<Point>();
		m_Rotations = 0;
		m_Orientation = Orientation.NORTH;
	}

	public ArrayList<Point> getWayOut() {
		return m_WayOut;
	}

	public void findWayOut() {
		if(m_X != 0 || m_Y != 0) {
			if(m_Rotations == 0) {
				if(!walk()) {
					followWall();
				}
			}
			else if(m_Rotations < -6 || m_Rotations > 6) {
				return;
			}
			else {
				followWall();
			}
			findWayOut();
		}
	}

	public void followWall() {
		turnRight();
		if(walk())
			return;

		turnLeft();
		if(walk())
			return;

		turnLeft();
		if(walk())
			return;

		turnLeft();
		if(walk())
			return;
	}
	
	public void turnLeft() {
		if(m_Orientation == Orientation.NORTH)
			m_Orientation = Orientation.WEST;
		else if(m_Orientation == Orientation.WEST)
			m_Orientation = Orientation.SOUTH;
		else if(m_Orientation == Orientation.SOUTH)
			m_Orientation = Orientation.EAST;
		else if(m_Orientation == Orientation.EAST)
			m_Orientation = Orientation.NORTH;
		
		m_Rotations--;
	}
	
	public void turnRight() {
		if(m_Orientation == Orientation.NORTH)
			m_Orientation = Orientation.EAST;
		else if(m_Orientation == Orientation.EAST)
			m_Orientation = Orientation.SOUTH;
		else if(m_Orientation == Orientation.SOUTH)
			m_Orientation = Orientation.WEST;
		else if(m_Orientation == Orientation.WEST)
			m_Orientation = Orientation.NORTH;
		
		m_Rotations++;
	}

	public boolean walk() {
		if(m_Maze.canWalk(m_X, m_Y, m_Orientation)) {
			if(m_Orientation == Orientation.NORTH)
				m_Y--;
			else if(m_Orientation == Orientation.WEST)
				m_X--;
			else if(m_Orientation == Orientation.SOUTH)
				m_Y++;
			else if(m_Orientation == Orientation.EAST)
				m_X++;
			
			m_WayOut.add(new Point(m_X, m_Y));
			return true;
		}
		return false;
	}

	/*wayOut = new ArrayList<Point>();

    int current_x = x;
    int current_y = y;
    Orientation prev_dir = Orientation.NORTH;
    int turn_counter = 0;
    int i = 0;

    while(i < 10 && (current_x != 0 || current_y != 0)) {
    	Orientation dir = null;

    	if(turn_counter == 0) {	// Walk straight
    		if(prev_dir == Orientation.NORTH && rooms[current_y][current_x].north.isGone) {
    			dir = Orientation.NORTH;
    		}
    		else if(prev_dir == Orientation.WEST && rooms[current_y][current_x].west.isGone) {
    			dir = Orientation.WEST;
    		}
    		else if(prev_dir == Orientation.SOUTH && rooms[current_y+1][current_x].north.isGone) {
    			dir = Orientation.SOUTH;
    		}
    		else if(prev_dir == Orientation.EAST && rooms[current_y][current_x+1].west.isGone) {
    			dir = Orientation.EAST;
    		}
    	}

    	while(dir == null) { //Cannot go straight ahead, turn left
    		if(prev_dir == Orientation.NORTH && rooms[current_y][current_x].west.isGone) {
        		dir = Orientation.WEST;
        	}
        	else if(prev_dir == Orientation.WEST && rooms[current_y+1][current_x].north.isGone) {
        		dir = Orientation.SOUTH;
        	}
        	else if(prev_dir == Orientation.SOUTH && rooms[current_y][current_x+1].west.isGone) {
        		dir = Orientation.EAST;
        	}
        	else if(prev_dir == Orientation.EAST && rooms[current_y][current_x].north.isGone) {
        		dir = Orientation.NORTH;
        	}
    		//Turn left
    		turn_counter--;
    	}
		System.out.println("TURN: "+turn_counter);

    	/*if(dir == null) {
    		if(prev_dir == WalkDirection.NORTH)
        		dir = WalkDirection.SOUTH;
    		else if(prev_dir == WalkDirection.WEST)
        		dir = WalkDirection.EAST;
    		else if(prev_dir == WalkDirection.SOUTH)
        		dir = WalkDirection.NORTH;
    		else if(prev_dir == WalkDirection.EAST)
        		dir = WalkDirection.WEST;
    	}/

		prev_dir = dir;
    	if(dir != null) {
    		if(dir == Orientation.NORTH)
    			current_y--;
    		else if(dir == Orientation.WEST)
    			current_x--;
    		else if(dir == Orientation.SOUTH)
    			current_y++;
    		else if(dir == Orientation.EAST)
    			current_x++;
    	}
    	else
    		break;

		wayOut.add(new Point(current_x, current_y));
    }*/
}
