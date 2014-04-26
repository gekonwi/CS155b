package cs155.jray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class represents a group of Objects. Once a collection of objects are
 * grouped they can be transformed by a single transform operator!
 **/
public class Group3D extends Object3D {

	private static double INTERSECTION_DIFF = 0.01;

	private ArrayList<Object3D> objs = new ArrayList<Object3D>();

	public Group3D() {
	}

	public Group3D(Object3D[] objs) {
		this.add(objs);
	}

	public void clear() {
		objs = new ArrayList<Object3D>();
	}

	public void add(Object3D obj) {
		objs.add(obj);
	}

	public void add(Object3D[] obj) {
		for (int i = 0; i < obj.length; i++)
			objs.add(obj[i]);
	}

	// x.rayIntersect(ray) -- returns a double, POSITIVE_INFINITY for no
	// intersection, t>0 for intersection at value t
	public List<RayHit> rayIntersects(Ray3D ray) {
		List<RayHit> allHits = new ArrayList<>();

		// collect intersections with objects in the scene
		for (Object3D obj : objs) {
			RayHit hit = obj.rayIntersect(ray);
			if (hit != RayHit.NO_HIT)
				allHits.add(hit);
		}

		Collections.sort(allHits, new Comparator<RayHit>() {

			@Override
			public int compare(RayHit o1, RayHit o2) {
				return Double.compare(o1.distance, o2.distance);
			}
		});

		if (allHits.isEmpty())
			return allHits;

		double maxDist = allHits.get(0).distance + INTERSECTION_DIFF;
		List<RayHit> closestHits = new ArrayList<>();

		for (RayHit hit : allHits)
			if (hit.distance <= maxDist)
				closestHits.add(hit);

		if (closestHits.size() > 1)
			System.out.println("***** close hitpoints: " + closestHits.size());

		return closestHits;
	}

	@Override
	public RayHit rayIntersect(Ray3D ray) {
		throw new UnsupportedOperationException(
				"use rayIntersects(...) instead");
	}

}
