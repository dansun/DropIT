package nu.danielsundberg.droid.dropit.rendering.object;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;

public class TexturedBox extends MyRenderableObject {
	public Texture t;
	
	@Override
	public void draw(GL10 gl, float posx, float posy, float rotationRadians, Mesh mesh) {
		t.bind();
		gl.glDisable(GL10.GL_COLOR_MATERIAL);
		super.draw(gl, posx, posy, rotationRadians, mesh);
	}

	public TexturedBox(float x1, float y1, float rotation, Texture t, float u1, float v1, float u2, float v2) {
		this.t = t;
		/** The initial vertex definition */
		vertices = new float[]{
				// vertice[x,x,x] normal [x,x,x] texture[x,x]
				-3f,-3f,-3f, 0f, 0f, 1f, 0f, 0f,           // 0
				 3f,-3f,-3f, 0f, 0f, 1f, 0f, 0f,           // 1
				-3f, 3f,-3f, 0f, 0f, 1f, 0f, 0f,           // 2
				 3f, 3f,-3f, 0f, 0f, 1f, 0f, 0f,           // 3

				 3f,-3f, 3f,-1f, 0f, 0f, 0f, 0f,           // 4 
			 	 3f,-3f,-3f,-1f, 0f, 0f, 0f, 0f,           // 5
				 3f, 3f, 3f,-1f, 0f, 0f, 0f, 0f,           // 6
				 3f, 3f,-3f,-1f, 0f, 0f, 0f, 0f,           // 7

			     3f,-3f, 3f, 0f, 0f,-1f, u2, v2,           // 8
			    -3f,-3f, 3f, 0f, 0f,-1f, u1, v2,           // 9
				 3f, 3f, 3f, 0f, 0f,-1f, u2, v1,           // 10
				-3f, 3f, 3f, 0f, 0f,-1f, u1, v1,           // 11

				-3f,-3f,-3f, 1f, 0f, 0f, 0f, 0f,           // 12
				-3f,-3f, 3f, 1f, 0f, 0f, 0f, 0f,           // 13
				-3f, 3f,-3f, 1f, 0f, 0f, 0f, 0f,           // 14
				-3f, 3f, 3f, 1f, 0f, 0f, 0f, 0f,           // 15

				-3f,-3f,-3f, 0f,-1f, 0f, 0f, 0f,           // 16
				 3f,-3f,-3f, 0f,-1f, 0f, 0f, 0f,           // 17
				-3f,-3f, 3f, 0f,-1f, 0f, 0f, 0f,           // 18
				 3f,-3f, 3f, 0f,-1f, 0f, 0f, 0f,           // 19

				-3f, 3f, 3f, 0f, 1f, 0f, 0f, 0f,           // 20
				 3f, 3f, 3f, 0f, 1f, 0f, 0f, 0f,           // 21
				-3f, 3f,-3f, 0f, 1f, 0f, 0f, 0f,           // 22
				 3f, 3f,-3f, 0f, 1f, 0f, 0f, 0f            // 23
		};
		
		/** The initial indices definition */
		indices = new short[]{
				// Faces definition
				 0, 1, 3,  0, 3, 2, 			//Face front
	    		 4, 5, 7,  4, 7, 6, 			//Face right
	    		11, 9, 8, 10,11, 8, 		//... 
	    		12,13,15, 12,15,14, 	
	    		16,17,19, 16,19,18, 	
	    		20,21,23, 20,23,22
		};
		
	}	
}
