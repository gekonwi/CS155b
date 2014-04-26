package cs155.jray;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This helper class offers a convenient way to show a scene.
 * 
 * @author georg
 * 
 */
public class Demo {
	public static void show(Scene3D scene) {
		final NewRayCanvas3D mc = new NewRayCanvas3D(scene, 800, 800);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI(mc);
			}
		});

		RayTracer3D.drawScene(scene);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mc.refresh();
			}
		});
	}

	private static void createAndShowGUI(NewRayCanvas3D mc) {
		JFrame f = new JFrame("PA05 Demo");

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(mc);
		f.setSize(800, 800);
		f.setVisible(true);
	}
}
