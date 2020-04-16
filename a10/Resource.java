package a10;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Resource implements ActionListener {

	private static int resourceAmount = 0;
	private static int coolDownCounter;
	private static int coolDown = 300;
	private static Timer timer;

	public String getCoolDown() {
		String coolDownString = coolDownCounter + "";
		return coolDownString;
	}

	public void resetCoolDown() {
		coolDownCounter = coolDown;
	}

	public void decrementCoolDown() {
		coolDownCounter--;
	}

	public void update() {
		decrementCoolDown();
	}

	public boolean readyForAction() {
		if (coolDownCounter <= 0)
			return true;
		return false;
	}

	public int getResource() {
		return resourceAmount;
	}

	public void setResource(int resourceSet) {
		resourceAmount = resourceSet;
	}

	public void addResource(int resourceAdd) {
		resourceAmount += resourceAdd;
	}

	public String toString() {
		return "" + getResource();
	}

	public Resource() {
		timer = new Timer(300, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
//		System.out.println(toString());
		this.update();

		if (this.readyForAction() == true) {
			this.addResource(5);
			this.resetCoolDown();
		}
		
	}

	public static void main(String args[]) {
	}
}
