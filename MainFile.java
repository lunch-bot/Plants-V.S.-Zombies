package a10;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainFile extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Timer timer;
	private ArrayList<Actor> actors; // Plants and zombies all go in here
	BufferedImage plantImage; // Maybe these images should be in those classes, but easy to change here.
	BufferedImage zombieImage;
	BufferedImage projectileImage;
	int numRows;
	int numCols;
	int cellSize;
	boolean gameOver = false;
	private JFrame app;

	/**
	 * Setup the basic info for the example
	 */
	public MainFile(JFrame app) 
	{
		super();
		Random chance = new Random(); //used for random rows and columns
		this.app = app;

		// Define some quantities of the scene
		numRows = 5;
		numCols = 7;
		cellSize = 75;
		setPreferredSize(new Dimension(50 + numCols * cellSize, 50 + numRows * cellSize));

		// Store all the plants and zombies in here.
		actors = new ArrayList<>();

		// Load images
		try 
		{
			plantImage = ImageIO.read(new File("src/a10/Animal-Icons/frog-icon.png"));
			zombieImage = ImageIO.read(new File("src/a10/Animal-Icons/chihuahua-icon.png"));
			projectileImage = ImageIO.read(new File("src/a10/Animal-Icons/crab-icon.png"));
		} 
		catch (IOException e) 
		{
			System.out.println("A file was not found");
			System.exit(0);
		}

		// Starting Plant
		Plant plant = new Plant(new Point2D.Double(200, 225),
				new Point2D.Double(plantImage.getWidth(), plantImage.getHeight()), plantImage, 100, 30, 1);
		
		// Starting Zombie(s)
		int chanceToAppear = chance.nextInt(100);
		if (chanceToAppear >= 50) 
		{
			Random randomRow = new Random();
			int randomRowYPos = (randomRow.nextInt(5)) * 75;
			Zombie zombie3 = new Zombie(new Point2D.Double(500, randomRowYPos),
					new Point2D.Double(zombieImage.getWidth(), zombieImage.getHeight()), zombieImage, 100, 30, -0.5, 1);
			actors.add(zombie3);
		}
		
		// Make a zombie in a random row
		Random randomRow = new Random();
		int randomRowYPos = (randomRow.nextInt(5)) * 75;
		Zombie zombie2 = new Zombie(new Point2D.Double(500, randomRowYPos),
				new Point2D.Double(zombieImage.getWidth(), zombieImage.getHeight()), zombieImage, 100, 30, -0.5, 1);

		//Create Projectiles
		Projectile projectile = new Projectile(new Point2D.Double(200, 225), new Point2D.Double(projectileImage.getWidth(), projectileImage.getHeight()), projectileImage, 1, 1, 5, 50);

		
		// Add them to the list of actors
		actors.add(plant);
		actors.add(zombie2);
		actors.add(projectile);

		// The timer updates the game each time it goes.
		// Get the javax.swing Timer, not from util.
		timer = new Timer(5, this);
		timer.start();
		}
	

	/***
	 * Implement the paint method to draw the plants
	 */
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		for (Actor actor : actors) {
			actor.draw(g, 0);
			actor.drawHealthBar(g);
		}
	}

	/**
	 * 
	 * This is triggered by the timer. It is the game loop of this test.
	 * 
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// This method is getting a little long, but it is mostly loop code
		// Increment their cooldowns and reset collision status
		for (Actor actor : actors) 
		{
			actor.update();
		}

		// Try to attack
		for (Actor actor : actors) 
		{
			for (Actor other : actors) 
			{
				actor.attack(other);
			}
		}

		// Remove plants and zombies with low health
		ArrayList<Actor> nextTurnActors = new ArrayList<>();
		for (Actor actor : actors) 
		{
			if (actor.isAlive()) 
			{
				nextTurnActors.add(actor);
			}
			else 
			{
				actor.removeAction(actors); // any special effect or whatever on removal
			}
		}
		actors = nextTurnActors;

		// Check for collisions between zombies and plants and set collision status
		for (Actor actor : actors) 
		{
			for (Actor other : actors) 
			{
				actor.setCollisionStatus(other);
			}
		}

		// Move the actors.
		for (Actor actor : actors) 
		{
			actor.move(); // only moves if it is not colliding
		}
		
		for (Actor actor : actors) 
		{
			if (actor.getPosition().getX() < 0)
			{
				System.out.println("You died oof"); //shows that the program is actually working
				this.gameOver = true;
				timer.stop();
				app.dispose();
				return;

			}
		}
		
		

		// Redraw the new scene
		repaint();
	}

	/**
	 * Make the game and run it
	 * 
	 * @param args
	 */
	public static void main(String[] args) //actually runs the game, does not do the game loop however
	{
		// Schedule a job for the event-dispatching thread:
		javax.swing.SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				System.out.println("Running Plants V.S. Zombies");
				System.out.println("Created by Guyan Cool and Alexander Thomsen");
				JFrame app = new JFrame("Plants V.S. Zombies"); //Name of the Program
				app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Exits (Disposes and Exits (use DISPOSE_ON_CLOSE for only Dispose) of the JPanel when pressing the exit button
				MainFile panel = new MainFile(app);

				app.setContentPane(panel);
				app.pack();
				app.setVisible(true); //allows you to actually see the window

			}
		});

	}

}