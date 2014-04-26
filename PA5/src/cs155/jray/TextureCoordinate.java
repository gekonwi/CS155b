package cs155.jray;

/**
   A pair of doubles returned as part of a RayHit object
   which represent the location of the intersection in 2D texture space
 **/
public class TextureCoordinate {
    double x,y;  // raw coordinates

    public TextureCoordinate(double x, double y) {
	this.x = x; this.y = y;
    }

}
