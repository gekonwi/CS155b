package cs155.jray;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;

import java.awt.Color;

/**
 * This class tests out the methods in PA01
 * 
 * @author tim
 * 
 */
public class PA06Demo {
	private static Scene3D scene = new Scene3D();
	private static NewRayCanvas3D mc = new NewRayCanvas3D(scene, 800, 800);

	/**
	 * this creates a window to demo the Canvas3D object
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		/*
		 * This is the preferred way to create a GUI. It avoid thread problems
		 * by creating the GUI in the EventDispatch thread.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

		System.out.println("getting ready to draw scene");
		Thread.sleep(2000L);
		mc.scene = Demos.myDemo();

		RayTracer3D.drawScene(mc.scene);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mc.refresh();
			}
		});
		// mc.refresh();
		// mc.invalidate();
		System.out.println("drew a circle!");
	}

	/*
	 * here we create a window, add the canvas, set the window size and make it
	 * visible!
	 */
	private static void createAndShowGUI() {

		JFrame f = new JFrame("PA06 Demo");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mc);
		f.setSize(800, 800);
		f.setVisible(true);
	}




}
