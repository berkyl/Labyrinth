import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Maze extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Room[][] rooms;// m x n matrix of rooms
	private ArrayList<Wall> walls; // List of walls
	private Random rand;// for random wall
	private int height;// height of matrix
	private int width;// width of matrix
	private int num;// incrementor
	private JoinRoom ds;// union paths

	// paint methods //
	private int x_cord; // x-axis rep
	private int y_cord;// y-axis rep
	private int roomSize;
	private int randomWall;
	
	private Point selectedCell;
	private Person person;
	
	public Maze(int height, int width) {
		this.height = height;
		this.width = width;
		rooms = new Room[height][width];
		walls = new ArrayList<Wall>((height - 1) * (width - 1));
		generateRandomMaze();
		
		person = new Person(this, 0, 0);
		
		MouseAdapter mouseHandler;
        mouseHandler = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                int width = getWidth();
                int height = getHeight();

                int cellWidth = (width - x_cord) / width + 7;
                int cellHeight = (height - x_cord) / height + 7;

                int x = (e.getX()) / cellWidth;
                int y = (e.getY()) / cellHeight;

                selectedCell = new Point(x, y);
                
                /*rooms[row][column].north.isGone = true;
                rooms[row][column].west.isGone = true;*/
                
                person.setPosition(x, y);
               
                
        		//System.out.println(row + " <-> " +column);
        		//System.out.println(rooms[row][column].x  + " " + rooms[row][column].y);
                //for(int i = 0; i < 1; i++) {

        		//rooms[y][x].north.isGone = true;
                /*int prev_x, current_x, prev_y, current_y;
                prev_x = current_x = x;
                prev_y = current_y = y;
                int i = 0;
                while(current_x != 0 || current_y != 0) {
                	WalkDirection dir = null;
                	if(rooms[current_y][current_x].north.isGone && current_y -1 != prev_y) {// can go north;
                		dir = WalkDirection.NORTH;
                	}
                	else if(rooms[current_y][current_x].west.isGone && current_x -1 != prev_x) {// can go west;
                		dir = WalkDirection.WEST;
                	}
                	else if(rooms[current_y+1][current_x].north.isGone && current_y +1 != prev_y) {// can go south;
                		dir = WalkDirection.SOUTH;
                	}
                	else if(rooms[current_y][current_x+1].west.isGone && current_x +1 != prev_x) {// can go east;
                		dir = WalkDirection.EAST;
                	}

                	prev_x = current_x;
                	prev_y = current_y;
                	
                	if(dir != null) {
                		if(dir == WalkDirection.NORTH)
                			current_y--;
                		else if(dir == WalkDirection.WEST)
                			current_x--;
                		else if(dir == WalkDirection.SOUTH)
                			current_y++;
                		else if(dir == WalkDirection.EAST)
                			current_x++;
                	}
                	else
                		break;
                	
            		wayOut.add(new Point(current_x, current_y));
            		i++;
                }*/
                
                //}
                //rooms[row][column].east.isGone = true;
                //rooms[row][column].south.isGone = true;
                /*if(){
                	
                }*/
                
                
                repaint();

            }
        };
        addMouseMotionListener(mouseHandler);
		
		setPreferredSize(new Dimension(800, 700));
	}
	private void generateRandomMaze() {
		generateInitialRooms();// see next method
		ds = new JoinRoom(width * height);
		rand = new Random(); // here is the random room generator
		num = width * height;

		while (num > 1) {
			// when we pick a random wall we want to avoid the borders getting eliminated
			randomWall = rand.nextInt(walls.size());
			Wall temp = walls.get(randomWall);
			// we will pick two rooms randomly 
			int roomA = temp.currentRoom.y + temp.currentRoom.x * width;
			int roomB = temp.nextRoom.y + temp.nextRoom.x * width;

			// check roomA and roomB to see if they are already members 
			if (ds.find(roomA) != ds.find(roomB)) {
				walls.remove(randomWall);
				ds.unionRooms(ds.find(roomA), ds.find(roomB));
				temp.isGone = true;
				temp.currentRoom.adj.add(temp.nextRoom);
				temp.nextRoom.adj.add(temp.currentRoom);
				num--;
			}// end of if
		}// end of while
	}

	// name the room to display
	private int roomNumber = 0;
	/**
	 * Sets the grid of rooms to be initially boxes
	 * This is self explanitory, we are only creating an reverse L for all
	 * The rooms and there is an L for the border
	 */
	private void generateInitialRooms() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// create north walls
				rooms[i][j] = new Room(i, j);
				if (i == 0) {
					rooms[i][j].north = new Wall(rooms[i][j]);
				} else {
					rooms[i][j].north = new Wall(rooms[i - 1][j], rooms[i][j]);
					walls.add(rooms[i][j].north);
				}
				if (i == height - 1) {
					rooms[i][j].south = new Wall(rooms[i][j]);
				}
				if (j == 0) {
					rooms[i][j].west = new Wall(rooms[i][j]);
				} else {
					rooms[i][j].west = new Wall(rooms[i][j - 1], rooms[i][j]);
					walls.add(rooms[i][j].west);
				}
				if (j == width - 1) {
					rooms[i][j].east = new Wall(rooms[i][j]);
				}
				rooms[i][j].roomName = roomNumber++;// we will name the rooms
			}
		}
		// initalize entrance and exit
		rooms[0][0].west.isGone = true;// you can replace .west.isGone with .north.isGone
		// this is just saying the roomName for top left is 0 
		rooms[0][0].roomName = 0;
		// we will remove the south wall of the last room
		rooms[height - 1][width - 1].south.isGone = true;
		// this is just saying the roomName for bottom right is the last element in the mxn room matrix
		rooms[height - 1][width - 1].roomName = (height * width);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		x_cord = 0;
		y_cord = 0;
		// could have taken height as well as width
		// just need something to base the roomsize
		roomSize = (width - x_cord) / width + 7;

		// temp variables used for painting
		int x = x_cord;
		int y = y_cord;
		
		if(person != null && person.getX() < width && person.getY() < height) {
            person.findWayOut();
			ArrayList<Point> wayOut = person.getWayOut();
			for(int i = 0; i < wayOut.size(); i++) {
				g.setColor(Color.RED);
				g.fillOval(wayOut.get(i).x * roomSize, wayOut.get(i).y * roomSize, roomSize, roomSize);
				g.setColor(Color.BLACK);
			}
		}

		if(selectedCell != null) {
			g.setColor(Color.BLUE);
			g.fillOval(selectedCell.x*roomSize, selectedCell.y*roomSize, roomSize, roomSize);
			g.setColor(Color.BLACK);
			//System.out.println(rooms[i][j].prevgetRoomName());
		}
		
		for (int i = 0; i <= height - 1; i++) {
			for (int j = 0; j <= width - 1; j++) {
				if (!(rooms[i][j].north.isGone)) {
					g.drawLine(x, y, x + roomSize, y);
				}//end of north if
				// west wall not there draw the line
				if (!(rooms[i][j].west.isGone)) {
					g.drawLine(x, y, x, y + roomSize);
				}// end of west if
				if ((i == height - 1) && rooms[i][j].south.isGone == false) {
					g.drawLine(x, y + roomSize, x + roomSize,
							y + roomSize);
				}// end of south if
				if ((j == width - 1) && rooms[i][j].east.isGone == false) {
					g.drawLine(x + roomSize, y, x + roomSize,
							y + roomSize);
				}// end of east if
				
				x += roomSize;// change the horizontal
			}// end of inner for loop
			x = x_cord;
			y += roomSize;
		}// end of outer for loop
	}

	public boolean canWalk(int x, int y, Orientation direction) {
		if(x >= width || y >= height)
			return false;
		
		if(direction == Orientation.NORTH && rooms[y][x].north.isGone) {
			return true;
		}
		else if(direction == Orientation.WEST && rooms[y][x].west.isGone) {
			return true;
		}
		else if(direction == Orientation.SOUTH && y+1 < height && rooms[y+1][x].north.isGone) {
			return true;
		}
		else if(direction == Orientation.EAST &&  x+1 < width && rooms[y][x+1].west.isGone) {
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		// we will use the scanner for userInput
		Scanner userInput = new Scanner(System.in);
		int m, n;// these are variables for the size of maze (m x n)
		System.out.print("Enter the size of your maze: ");
		// store the input
		m = userInput.nextInt();
		n = userInput.nextInt();
		// use JFrame to put the created panel on
		System.out.println(m+ " - "+ n);
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 800);
		frame.getContentPane().add(new Maze(m, n));
		frame.pack();
		frame.setVisible(true);
	}// end of main
}// END OF CLASS 