package cs155.opengl;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrianglePrismTest {

	@Test
	public void testExpandPoints() {
		float[] vertices = {
			0f, 1f, 2f,
			3f, 4f, 5f,
			6f, 7f, 8f,
			9f, 10f, 11f
		};
		
		byte[] indices = {
				1, 2, 0, 3, 2, 2
		};
		
		float[] expected = {
				3f, 4f, 5f,
				6f, 7f, 8f,
				0f, 1f, 2f,
				9f, 10f, 11f,
				6f, 7f, 8f,
				6f, 7f, 8f
			};	
		
		float[] actual = TrianglePrism.expandPoints(vertices, indices);
		
		assertEquals(expected.length, actual.length);
		
		for (int i = 0; i < expected.length; i++)
			assertEquals(expected[i], actual[i], 0.0001);
	}
	
}
