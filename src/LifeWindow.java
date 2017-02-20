/***********************************************
 * 
 * Ger Thao
 * Thao248
 * Computer Science 251
 * Program 10
 * May 6 2014
 * 
 * Conway's Game of Life Simulation Program
 * 
 * Creates a UI which a 2D grid of cells, each of which is either alive or dead,
 * and simulates the growth/death of these cells through generations.
 * 
 * 
 ***********************************************/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class LifeWindow extends JFrame{
	
	//I don't think I actually used the BasicSimulation besides from having it instantiate the LifeWindow Class
	private JButton resize, stop, reset, quit;
	private BasicSimulation game;
	private JLabel row, column, gen;
	private JTextField rBox, cBox, gBox;
	private JPanel gamePanel, controls = new JPanel(new GridLayout(3, 3));
	private boolean[][] isAlive;
	private JButton[][] squares;
	private int numOfColumns, numOfRows, numOfGenerations;
	private static final long serialVersionUID = 1L;
	private Timer timer;
	
	
	
	
	public LifeWindow(BasicSimulation basicSimulation, int i, int j) {
		game = basicSimulation;
		setup();
		setVisible(true);
	}
	private void setup(){
		setTitle("Life");
		setSize(900, 900);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		createContents();
		setLayout();
		addListeners();
	}
	private void setLayout(){
		setLayout(new BorderLayout());
		JPanel controls = new JPanel(new GridLayout(3,3));
		JPanel rightPanel = new JPanel(new FlowLayout());
		
		JPanel rowP = new JPanel(new BorderLayout());
		rowP.add(pad(row,rBox), BorderLayout.NORTH);
		rowP.add(pad(FlowLayout.RIGHT), BorderLayout.CENTER);
		
		JPanel colP = new JPanel(new BorderLayout());
		colP.add(pad(column, cBox), BorderLayout.NORTH);
		colP.add(pad(FlowLayout.RIGHT),BorderLayout.CENTER);
		
		JPanel resizeP = new JPanel(new BorderLayout());
		resizeP.add(pad(resize), BorderLayout.NORTH);
		resizeP.add(pad(FlowLayout.RIGHT));

		JPanel generationP = new JPanel(new BorderLayout());
		generationP.add(pad(gen,gBox),BorderLayout.NORTH);
		generationP.add(pad(FlowLayout.RIGHT));
			
		JPanel stopP = new JPanel(new BorderLayout());
		stopP.add(pad(stop), BorderLayout.NORTH);
		stopP.add(pad(FlowLayout.RIGHT));

		JPanel resetP = new JPanel(new BorderLayout());
		resetP.add(pad(reset), BorderLayout.NORTH);
		resetP.add(pad(FlowLayout.RIGHT));
		
		JPanel quitP = new JPanel(new BorderLayout());
		quitP.add(pad(quit), BorderLayout.NORTH);
		quitP.add(pad(FlowLayout.RIGHT));
		
		controls.add(rowP);
		controls.add(colP);
		controls.add(resizeP);
		controls.add(generationP);
		controls.add(new JPanel());
		controls.add(new JPanel());
		controls.add(stopP);
		controls.add(resetP);
		controls.add(quitP);
		
		rightPanel.add(controls);
		add(rightPanel, BorderLayout.SOUTH);
		makeSquare();
		add(gamePanel, BorderLayout.CENTER);
	}
	
	private void createContents(){
		resize = new JButton("Resize");
		stop = new JButton("Start");
		reset = new JButton("Reset");
		quit = new JButton("Quit");
		
		rBox = new JTextField(10);
		cBox = new JTextField(10);
		gBox = new JTextField(10);
		
		rBox.setText("10");
		cBox.setText("10");
		gBox.setText("10");
		
		row = new JLabel("Rows");
		column = new JLabel("Columns");
		gen = new JLabel("Generations");
		
		gamePanel = new JPanel();	
		
		add(gamePanel, BorderLayout.WEST);
		add(controls, BorderLayout.EAST);
	}
	
	private JPanel pad(JComponent... stuff){
		return pad(FlowLayout.LEFT,stuff);
	}
	
	private JPanel pad(int justification, JComponent... stuff){
		JPanel p = new JPanel(new FlowLayout(justification));
		for(JComponent comp: stuff)
			p.add(comp);
		return p;
	}
	
	private void makeSquare(){	
		Graphics g = gamePanel.getGraphics();
		numOfRows = get(rBox.getText());
		numOfColumns = get(cBox.getText());
		numOfGenerations = get(gBox.getText());
		isAlive = new boolean[numOfColumns][numOfRows];
		squares = new JButton[numOfColumns][numOfRows];
		gamePanel.setLayout(new GridLayout(numOfRows, numOfColumns, 1 , 1));
		for (int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){
			squares[i][j] = new JButton();
			gamePanel.add(squares[i][j]);
			if(i == numOfColumns - 1){
				j++;
				i = -1;
			}
		}
		for (int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){
			if(!isAlive[i][j]){	//i don't think the if statement is needed, but otherwise this sets the color of the cells first
				squares[i][j].setBackground(Color.BLUE);
			}
			else{
				squares[i][j].setBackground(Color.RED);
			}
			if(i == numOfColumns - 1){
				j++;
				i = -1;
			}
		}
	}
	
	private int get(String value){	//makes sure that the value will always be an integer
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException e){
			return 1;
		}
	}
	
	private void addListeners(){
		final MouseListener ml = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){	//allows the cells to be toggled
				if(e.getComponent().getBackground() == Color.BLUE){
					e.getComponent().setBackground(Color.RED);
				}
				else e.getComponent().setBackground(Color.BLUE);
			}	
		};
		resize.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				int va1 = get(rBox.getText());
				int va2 = get(cBox.getText());
				if(va1 <= 0 || va2 <= 0){	//if the user uses a negative number
					rBox.setText("10");
					cBox.setText("10");
					gamePanel.removeAll();	//removes the components of the grid and re-adds them according to the new values
					gamePanel.setVisible(false);
					gamePanel.revalidate();
					makeSquare();
					gamePanel.setVisible(true);
					for( int i = 0, j = 0; i < get(cBox.getText()) && j < get(rBox.getText()); i++){	//adds mouse listeners to the cells
						squares[i][j].addMouseListener(ml);
						if(i == get(cBox.getText()) - 1){
							j++;
							i = -1;
						}
					}
				}else{
					rBox.setText(Integer.toString(va1));
					cBox.setText(Integer.toString(va2));
					gamePanel.removeAll();	//removes the components of the grid and re-adds them according to the new values
					gamePanel.setVisible(false);
					gamePanel.revalidate();
					makeSquare();
					gamePanel.setVisible(true);
					for( int i = 0, j = 0; i < get(cBox.getText()) && j < numOfRows; i++){	//adds mouse listeners to the cells
						squares[i][j].addMouseListener(ml);
						if(i == numOfColumns - 1){
							j++;
							i = -1;
						}
					}
				}
			}			
		});
		for( int i = 0, j = 0; i < get(cBox.getText()) && j < numOfRows; i++){
			squares[i][j].addMouseListener(ml);
			if(i == numOfColumns - 1){
				j++;
				i = -1;
			}
		}		
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				if(stop.getText() == "Stop"){
					stop.setText("Start");
					resize.setEnabled(true);
					reset.setEnabled(true);
					rBox.setEnabled(true);
					cBox.setEnabled(true);
					gBox.setEnabled(true);
					for( int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){	//re-enables the cells
						squares[i][j].addMouseListener(ml);
						squares[i][j].setEnabled(true);
						if(i == numOfColumns - 1){
							j++;
							i = -1;
						}
					}
				}
				else{
					stop.setText("Stop");
					for( int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){
						squares[i][j].removeMouseListener(ml);	//disables the cells
						squares[i][j].setEnabled(false);
						if(i == numOfColumns - 1){
							j++;
							i = -1;
						}
					}
					numOfGenerations = get(gBox.getText());
					resize.setEnabled(false);
					reset.setEnabled(false);
					rBox.setEnabled(false);
					cBox.setEnabled(false);
					gBox.setEnabled(false);
					timer = new Timer(250, new ActionListener(){
						public void actionPerformed(ActionEvent e){
							start();
							numOfGenerations--;
							if(numOfGenerations <= 0) timer.stop();
						}
					});
					timer.start();
				}
			}
		});
		quit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				System.exit(0);				
			}			
		});
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				LifeWindow.this.dispose();
				new LifeWindow(new BasicSimulation(500), 50, 50);
			}
		});
	}
	private void start(){	//the method that plays the simulation
		for( int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){
			int count = 0;
			if(squares[i][j].getBackground() == Color.RED || squares[i][j].getBackground() == Color.BLUE){	//this determines what's alive/dead in the next generation
				if(i == numOfColumns-1 && j == 0){
					if(squares[i-1][j].getBackground() == Color.RED) count++;
					if(squares[i-1][j+1].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
				}
				else if(i == 0 && j == 0){
					if(squares[i+1][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
				}
				else if(i == 0 && j == numOfRows-1){
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
				}
				else if(i == numOfColumns-1 && j == numOfRows-1){
					if(squares[i-1][j-1].getBackground() == Color.RED) count++;
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i-1][j].getBackground() == Color.RED) count++;
				}
				else if(i == 0 && (j != 0 && j != numOfRows-1)){
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
				}
				else if(i == numOfColumns-1 && (j != 0 && j != numOfRows-1)){
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i-1][j-1].getBackground() == Color.RED) count++;
					if(squares[i-1][j+1].getBackground() == Color.RED) count++;
					if(squares[i-1][j].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
				}
				else if(j == 0 && (i != 0 && i != numOfColumns-1)){
					if(squares[i-1][j].getBackground() == Color.RED) count++;
					if(squares[i-1][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j+1].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
				}
				else if(j == numOfRows-1 && (i != 0 && i != numOfColumns-1)){
					if(squares[i-1][j].getBackground() == Color.RED) count++;
					if(squares[i-1][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j-1].getBackground() == Color.RED) count++;
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
				}
				else{
					if(squares[i-1][j].getBackground() == Color.RED) count++;
					if(squares[i][j-1].getBackground() == Color.RED) count++;
					if(squares[i-1][j-1].getBackground() == Color.RED) count++;
					if(squares[i+1][j-1].getBackground() == Color.RED) count++;
					if(squares[i-1][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j+1].getBackground() == Color.RED) count++;
					if(squares[i+1][j].getBackground() == Color.RED) count++;
					if(squares[i][j+1].getBackground() == Color.RED) count++;
				}
			}
			//count counts how many adjacent cells are alive
			if(count < 2 && squares[i][j].getBackground() == Color.RED) {isAlive[i][j] = false;}
			else if((count == 2 || count == 3) && squares[i][j].getBackground() == Color.RED) {isAlive[i][j] = true;}
			else if(count == 3 && squares[i][j].getBackground() == Color.BLUE) {isAlive[i][j] = true;}
			else {isAlive[i][j] = false;}
			if(i == numOfColumns - 1){
				j++;
				i = -1;
			}
		}
		for( int i = 0, j = 0; i < numOfColumns && j < numOfRows; i++){	//the boolean 2d array uses its values to determine the next state of each cell determined from the code above
			if(isAlive[i][j]) squares[i][j].setBackground(Color.RED);
			else squares[i][j].setBackground(Color.BLUE);
			if(i == numOfColumns - 1){
				j++;
				i = -1;
			}
		}
	}
}
