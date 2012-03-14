package nu.danielsundberg.droid.dropit.rendering.object;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;

public class MyRenderableObject {

	public float vertices[];
	public short indices[];
	public float normals[];
	
	// Translate params.
	public float x = 0;
	public float y = 0;
	public float z = 0;

	// Rotate params.
	public float rx = 0;
	public float ry = 0;
	public float rz = 0;
	
	public void draw(GL10 gl, float posx, float posy, float rotationRadians, Mesh mesh) {

		x = posx;
		y = posy;
		rz = (float) (180f/Math.PI)*rotationRadians;
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);
		mesh.render(GL10.GL_TRIANGLES, 0, mesh.getNumIndices());
		gl.glPopMatrix();

	}
}
