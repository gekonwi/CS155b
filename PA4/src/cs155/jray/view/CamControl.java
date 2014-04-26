package cs155.jray.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

import cs155.jray.RayCanvas3D;
import cs155.jray.RayTracer3D;
import cs155.jray.Transform3D;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public abstract class CamControl extends JFrame {

	private final class NavigationListener implements ActionListener {

		private Coord coord;
		private boolean add;

		public NavigationListener(Coord coord, boolean add) {
			super();
			this.coord = coord;
			this.add = add;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			double step = (Double) spinnerModel.getNumber();
			if (!add)
				step = -step;

			double x = (coord == Coord.x ? step : 0);
			double y = (coord == Coord.y ? step : 0);
			double z = (coord == Coord.z ? step : 0);

			rayCanvas.getScene().camera.apply(getTransform(x, y, z));
			
			System.out.println("new cam transform:");
			System.out.println(rayCanvas.getScene().camera.transform);
			
			RayTracer3D.drawScene(rayCanvas.getScene());
			rayCanvas.refresh();
		}
	}

	private JPanel contentPane;
	protected RayCanvas3D rayCanvas;

	protected JButton btnMoreX;
	protected JButton btnMoreY;
	protected JButton btnMoreZ;
	protected JButton btnLessX;
	protected JButton btnLessY;
	protected JButton btnLessZ;
	private JPanel panel;
	protected SpinnerNumberModel spinnerModel;

	protected enum Coord {
		x, y, z;
	}

	/**
	 * Create the frame.
	 */
	public CamControl(RayCanvas3D canvas, String title) {
		this.rayCanvas = canvas;
		buildView(title);
		setListeners();
	}

	abstract protected Transform3D getTransform(double x, double y, double z);

	private void setListeners() {
		btnMoreX.addActionListener(new NavigationListener(Coord.x, true));
		btnMoreY.addActionListener(new NavigationListener(Coord.y, true));
		btnMoreZ.addActionListener(new NavigationListener(Coord.z, true));

		btnLessX.addActionListener(new NavigationListener(Coord.x, false));
		btnLessY.addActionListener(new NavigationListener(Coord.y, false));
		btnLessZ.addActionListener(new NavigationListener(Coord.z, false));
	}

	private void buildView(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 202);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 73, 76, 88, 0 };
		gbl_contentPane.rowHeights = new int[] { 29, 29, 30, 29, 29, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		btnMoreX = new JButton("more x");
		btnMoreX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnMoreY = new JButton("more y");
		btnMoreY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnMoreY = new GridBagConstraints();
		gbc_btnMoreY.insets = new Insets(0, 0, 5, 5);
		gbc_btnMoreY.anchor = GridBagConstraints.NORTH;
		gbc_btnMoreY.gridx = 2;
		gbc_btnMoreY.gridy = 0;
		contentPane.add(btnMoreY, gbc_btnMoreY);

		btnMoreZ = new JButton("more z");
		GridBagConstraints gbc_btnMoreZ = new GridBagConstraints();
		gbc_btnMoreZ.insets = new Insets(0, 0, 5, 5);
		gbc_btnMoreZ.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnMoreZ.gridx = 3;
		gbc_btnMoreZ.gridy = 1;
		contentPane.add(btnMoreZ, gbc_btnMoreZ);

		btnLessX = new JButton("less x");
		GridBagConstraints gbc_btnLessX = new GridBagConstraints();
		gbc_btnLessX.insets = new Insets(0, 0, 5, 5);
		gbc_btnLessX.anchor = GridBagConstraints.SOUTH;
		gbc_btnLessX.gridx = 0;
		gbc_btnLessX.gridy = 2;
		contentPane.add(btnLessX, gbc_btnLessX);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		contentPane.add(panel, gbc_panel);

		JLabel lblStep = new JLabel("Step");
		panel.add(lblStep);

		spinnerModel = new SpinnerNumberModel(1.0, 0.0, 100000.0, 1.0);
		JSpinner spinner = new JSpinner(spinnerModel);
		panel.add(spinner);
		GridBagConstraints gbc_btnMoreX = new GridBagConstraints();
		gbc_btnMoreX.insets = new Insets(0, 0, 5, 0);
		gbc_btnMoreX.anchor = GridBagConstraints.SOUTHWEST;
		gbc_btnMoreX.gridx = 4;
		gbc_btnMoreX.gridy = 2;
		contentPane.add(btnMoreX, gbc_btnMoreX);

		btnLessZ = new JButton("less z");
		GridBagConstraints gbc_btnLessZ = new GridBagConstraints();
		gbc_btnLessZ.insets = new Insets(0, 0, 5, 5);
		gbc_btnLessZ.anchor = GridBagConstraints.NORTHEAST;
		gbc_btnLessZ.gridx = 1;
		gbc_btnLessZ.gridy = 3;
		contentPane.add(btnLessZ, gbc_btnLessZ);

		btnLessY = new JButton("less y");
		GridBagConstraints gbc_btnLessY = new GridBagConstraints();
		gbc_btnLessY.insets = new Insets(0, 0, 0, 5);
		gbc_btnLessY.anchor = GridBagConstraints.NORTH;
		gbc_btnLessY.gridx = 2;
		gbc_btnLessY.gridy = 4;
		contentPane.add(btnLessY, gbc_btnLessY);
	}
}
