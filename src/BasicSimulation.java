import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;


public class BasicSimulation {

	private ArrayList<SimulationListener> listeners = new ArrayList<SimulationListener>();
	private Timer timer;
	
	public BasicSimulation(int msDelay){
		timer = new Timer(msDelay,
				new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						for(SimulationListener l : listeners)
							l.updateSimulation();
					}					
		});
	}
	
	private void updateAll(boolean started){
		for(SimulationListener l : listeners)
			l.changeExecutionStatus(started);
	}
	
	public void start(){
		updateAll(true);
		timer.start();
	}
	
	public void stop(){
		updateAll(false);
		timer.stop();
	}

	public void setDelay(int msDelay){
		timer.setDelay(msDelay);
	}
	
	public void addSimulationListener(SimulationListener l){
		listeners.add(l);
	}

	public void removeSimulationListener(SimulationListener l){
		listeners.remove(l);
	}
	
	public static void main(String args[]){
		new LifeWindow(new BasicSimulation(500), 50, 50);
		
	}
}
