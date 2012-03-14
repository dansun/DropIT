package nu.danielsundberg.droid.dropit.physics;

import nu.danielsundberg.droid.dropit.rendering.object.MyRenderableObject;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.physics.box2d.Body;

public class MyPhysicsActor {
	
	public Body boxDef;
	public MyRenderableObject object;
	public Mesh mesh;
	
	public MyPhysicsActor(MyRenderableObject object, Body boxDef) {
		this.boxDef = boxDef;
		this.object = object;
		mesh = new Mesh(true, object.indices.length, object.vertices.length, 
				new VertexAttribute(Usage.Position, 3, "the_position"),
				new VertexAttribute(Usage.Normal, 3, "the_normal"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "the_texcoords"));	
		mesh.setVertices(object.vertices);
		mesh.setIndices(object.indices);
	}
	
	public void draw(com.badlogic.gdx.graphics.GL10 gl) {
		this.object.draw(gl, boxDef.getPosition().x, boxDef.getPosition().y, boxDef.getAngle(), mesh);
	}
	
}
