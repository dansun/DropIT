package nu.danielsundberg.droid.dropit.rendering.object;


public class WhiteBox extends MyRenderableObject {

	public WhiteBox(float x1, float y1, float rotation) {
		vertices = new float[]{
				 -1f, -1f, 1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, 1.0f, 
				 1f, -1f, 1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
				 -1f, 1f, 1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, 1.0f,
				 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, 1.0f,

				 1f, -1f, 1f, 1f, 1f, 1f, 1f, 1.0f, 0.0f, 0.0f,
			 	 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1.0f, 0.0f, 0.0f,
				 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1.0f, 0.0f, 0.0f,
				 1f, 1f, -1f, 1f, 1f, 1f, 1f, 1.0f, 0.0f, 0.0f,

				  1f, -1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, -1.0f,
				 -1f, -1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, -1.0f,
				  1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, -1.0f,
				 -1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 0.0f, -1.0f,

				 -1f, -1f, -1f, 1f, 1f, 1f, 1f, -1.0f, 0.0f, 0.0f,
				 -1f, -1f, 1f, 1f, 1f, 1f, 1f, -1.0f, 0.0f, 0.0f,
				 -1f, 1f, -1f, 1f, 1f, 1f, 1f, -1.0f, 0.0f, 0.0f,
				 -1f, 1f, 1f, 1f, 1f, 1f, 1f, -1.0f, 0.0f, 0.0f,

				-1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, -1.0f, 0.0f,
				 1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, -1.0f, 0.0f,
				-1f, 1f,  1f, 1f, 1f, 1f, 1f, 0.0f, -1.0f, 0.0f,
				 1f, 1f,  1f, 1f, 1f, 1f, 1f, 0.0f, -1.0f, 0.0f,

				-1f, 1f,  1f, 1f, 1f, 1f, 1f, 0.0f, 1.0f, 0.0f,
				 1f, 1f,  1f, 1f, 1f, 1f, 1f, 0.0f, 1.0f, 0.0f,
				-1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 1.0f, 0.0f,
				 1f, 1f, -1f, 1f, 1f, 1f, 1f, 0.0f, 1.0f, 0.0f,
		};

		/** The initial indices definition */
		indices = new short[]{
				// Faces definition
				0, 1, 3, 0, 3, 2, 		// Face front
				4, 5, 7, 4, 7, 6, 		// Face right
				8, 9, 11, 8, 11, 10, 	// ...
				12, 13, 15, 12, 15, 14, 
				16, 17, 19, 16, 19, 18, 
				20, 21, 23, 20, 23, 22, 
		};
	}	
}
