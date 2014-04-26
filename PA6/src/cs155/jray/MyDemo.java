package cs155.jray;

import java.awt.Color;

public class MyDemo {
	public static Scene3D completeScene() {
		Scene3D scene = initScene();

		scene.add(createCam());

		scene.add(createLight());

		scene.add(createGroundPlane());
		scene.add(createSnowMan());
		scene.add(createHat());

		return scene;
	}

	private static Object3D createHat() {
		ObjReader r = new ObjReader("obj/wizards_hat.obj");
		Group3D hat = r.toGroup3D();

		ImageTexture tex = new ImageTexture("images/leather.jpg");
		tex.scale(10, 10);
		Material mat = new Material();
		mat.texture = tex;
		mat.texWeight = 0.5;
		mat.specular = new Color3D(0.1, 0.1, 0.1);
		hat.insideMat = mat;
		hat.insideMat = mat;

		TransformedObject3D tr_hat = new TransformedObject3D(hat);
		hat.pushTextures();
		tr_hat.setTransform(Transform3D.IDENTITY.scale(1.5, 1.5, 1.5)
				.translate(0.15, 11.2, -3.3).rotateX(10));

		return tr_hat;
	}

	private static Scene3D initScene() {
		final Scene3D scene = new Scene3D();
		scene.backgroundColor = Color3D.BLACK;
		scene.ambient = Color3D.WHITE;
		return scene;
	}

	private static Light3D createLight() {
		Light3D light = new Light3D(new Point3D(-70, 70, 0), 1);
		light.ambient = new Color3D(1, 1, 1);
		return light;
	}

	private static Group3D createSnowMan() {
		Group3D snowMan = new Group3D();

		Material snowSphereMat = createSnowManMat();

		Sphere3D bottomSphere = new Sphere3D(0, 4.5, -5, 5);
		bottomSphere.outsideMat = snowSphereMat;
		TransformedObject3D trBottomSphere = new TransformedObject3D(
				bottomSphere);
		trBottomSphere.setTransform(Transform3D.scaleTransf(1, 0.95, 1));
		snowMan.add(trBottomSphere);

		Sphere3D middleSphere = new Sphere3D(0, 12, -5, 3);
		middleSphere.outsideMat = snowSphereMat;
		TransformedObject3D trMiddleSphere = new TransformedObject3D(
				middleSphere);
		trMiddleSphere.setTransform(Transform3D.scaleTransf(1, 0.9, 1));
		snowMan.add(trMiddleSphere);

		Sphere3D topSphere = new Sphere3D(0, 15, -5, 2);
		topSphere.outsideMat = snowSphereMat;
		snowMan.add(topSphere);

		Material eyeMat = createEyeMat();
		final double eyeDepth = -3.2;

		Sphere3D rightEye = new Sphere3D(-0.7, 15.5, eyeDepth, 0.2);
		rightEye.outsideMat = eyeMat;
		snowMan.add(rightEye);

		Sphere3D leftEye = new Sphere3D(0.7, 15.5, eyeDepth, 0.2);
		leftEye.outsideMat = eyeMat;
		snowMan.add(leftEye);

		Sphere3D nose = new Sphere3D(0, 15, -0.5, 0.2);
		nose.outsideMat = createNoseMat();
		TransformedObject3D trNose = new TransformedObject3D(nose);
		trNose.setTransform(Transform3D.scaleTransf(1, 1, 7));
		snowMan.add(trNose);

		return snowMan;
	}

	private static Material createEyeMat() {
		ImageTexture snowTex = new ImageTexture("images/snow_grass.jpg");
		snowTex.scale(10, 10);

		Material mat = new Material();
		mat.specular = Color3D.BLACK;
		mat.diffuse = new Color3D(0.5, 0.5, 0.5);
		mat.texture = snowTex;
		mat.texWeight = 0.1;
		mat.reflect = 0;
		// mat.emissive = new Color3D(Color.yellow);
		return mat;

	}

	private static Material createNoseMat() {
		ImageTexture snowTex = new ImageTexture("images/carrot_texture.jpg");
		snowTex.scale(10, 10);

		Material mat = new Material();
		mat.specular = Color3D.BLACK;
		mat.texture = snowTex;
		mat.texWeight = 0.9;
		mat.reflect = 0;
		return mat;

	}

	private static Material createSnowManMat() {
		ImageTexture snowTex = new ImageTexture("images/snow_grass.jpg");
		snowTex.scale(50, 50);

		Material mat = new Material();
		// snowMat.ambient = Color3D.WHITE;
		// snowMat.diffuse = Color3D.WHITE;
		mat.ambient = new Color3D(0.2, 0.2, 0.2);
		mat.diffuse = new Color3D(0.5, 0.5, 0.5);
		mat.specular = Color3D.BLACK;
		mat.texture = snowTex;
		mat.texWeight = 0.5;
		mat.reflect = 0;
		return mat;
	}

	private static Camera3D createCam() {
		Film film = new Film(800, 800);
		Transform3D camTransf;

		// final position:
		camTransf = Transform3D.IDENTITY.translate(0, 10, 0).rotateY(30)
				.rotateX(15).translate(4, 0, 70);

		// from the side
		// camTransf = Transform3D.IDENTITY.translate(3, 10, 0).rotateY(90)
		// .rotateX(15).translate(0, 0, 70);

		// front:
//		 camTransf = Transform3D.IDENTITY.translate(0, 15, 0).rotateX(15)
//		 .translate(0, 0, 20);

		Camera3D cam = new Camera3D(film, camTransf);
		cam.screenDist = -5;

		return cam;
	}

	private static Plane3D createGroundPlane() {
		ImageTexture snowTex = new ImageTexture("images/snow_grass.jpg");
		snowTex.scale(20, 20);

		Material mat = new Material();
		mat.ambient = new Color3D(0.2, 0.2, 0.2);
		mat.diffuse = new Color3D(0.5, 0.5, 0.5);
		mat.specular = Color3D.BLACK;
		mat.texture = snowTex;
		mat.texWeight = 0.5;
		mat.reflect = 0;

		return new Plane3D(new Point3D(0, 0, 0), new Point3D(0, 1, 0), mat);
	}
}
