package cs155.jray;

import java.awt.*;
import javax.swing.*;

/**
 * This represents 3D lights with the full OpenGL color model. So there are
 * several parameters including Color3D values for the ambient, diffuse, and
 * specular illumination as well as the attenuation coefficients: attC, attL,
 * attQ - representing the Constant, Linear, and Quadratic components Finally we
 * include an intensity to make it easy to increase the brightness of a all
 * components of a light.
 **/

public class Light3D {
    /** location of the light **/
    public Point3D location = new Point3D(0d, 0d, 0d);
    private static Color3D dimGray = new Color3D(0.2, 0.2, 0.2); // lig

    /** intensity of the light **/
    public double intensity = 1.0;

    /** ambient, diffuse, and specular components of the light **/
    public Color3D ambient = dimGray, diffuse = Color3D.WHITE,
	    specular = Color3D.WHITE;

    /** attenuation coefficients: constant, linear, and quadratic **/
    public double attC = 1.0, attL = 0.001, attQ = 0.00001;

    /** create a default white light at the origin **/
    public Light3D() {
    }

    /** create a default white light at a specified location **/
    public Light3D(Point3D new_location) {
	location = new Point3D(new_location);
    }

    public Light3D(Point3D new_location, double intensity) {
	location = new Point3D(new_location);
	this.intensity = intensity;
    }

    public Light3D(Point3D new_location, double intensity, Color3D amb,
	    Color3D dif, Color3D sp) {
	location = new Point3D(new_location);
	this.intensity = intensity;
	this.ambient = amb;
	this.diffuse = dif;
	this.specular = sp;
    }

    /** this returns the spotlight intensity which is 1.0 for all non-spotlights **/
    public double spot(Point3D lookAt) {
	return 1.0;
    }

    /**
     * calculate the attentuation of the light based on its distance from the
     * illuminated point
     **/
    public double attenuation(double length) {
	return 1.0 / (attC + attL * length + attQ * length * length);
    }

    /**
     * calculate the specular intensity of the light
     **/

    public static double specular(Point3D lightVec, Point3D normal,
	    Point3D eyeVec, int hardness) {
	Point3D u = lightVec.normalize();
	Point3D e = eyeVec.normalize();
	Point3D w = u.add(e).normalize();
	if (u.dot(normal) > 0)
	    return Math.pow(w.dot(normal), hardness);
	else
	    return 0.0;
    }

    /**
     * calculate the diffuse intensity
     **/
    public static double diffuse(Point3D lightVec, Point3D normal) {
	double cosA = normal.dot(lightVec);
	if (cosA < 0)
	    return 0.0;
	else
	    return (cosA);
    }

}
