package cs155.jray.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
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

public class CamRotationControl extends CamControl {

	/**
	 * Create the frame.
	 */
	public CamRotationControl(RayCanvas3D canvas) {
		super(canvas, "Cam Rotation");
		this.rayCanvas = canvas;
	}

	@Override
	protected Transform3D getTransform(double x, double y, double z) {
		Transform3D transform = Transform3D.rotationX(x);
		transform = transform.rotateY(y);
		transform = transform.rotateZ(z);

		return transform;
	}
}
