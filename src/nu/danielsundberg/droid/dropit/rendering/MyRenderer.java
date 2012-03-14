package nu.danielsundberg.droid.dropit.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import nu.danielsundberg.droid.dropit.physics.MyPhysicsActor;
import nu.danielsundberg.droid.dropit.view.MyGlView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;

public class MyRenderer {
	
	private MyGlView view;

	/* The initial light values */
	private float[] lightAmbient = { 1f, 1f, 1f, 10.0f };
	private float[] lightDiffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
	private float[] lightSpecular = { 0.1f, 0.1f, 0.1f, 1.0f };
	private float[] lightPosition = { 75f, 50f, -100f, 1.0f };
	

	/* The buffers for our light values */
	public FloatBuffer lightAmbientBuffer;
	public FloatBuffer lightDiffuseBuffer;
	public FloatBuffer lightSpecularBuffer;
	public FloatBuffer lightPositionBuffer;
	
	private float[] background = {1f,1f,1f,1f};

	public MyRenderer(MyGlView view) {
		super();
		this.view = view;
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(lightSpecular.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightSpecularBuffer = byteBuf.asFloatBuffer();
		lightSpecularBuffer.put(lightSpecular);
		lightSpecularBuffer.position(0);

	}	

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		// Calculate The Aspect Ratio Of The Window
		Gdx.graphics.getGLU().gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 1000.0f);

	}

	public void onDrawFrame(com.badlogic.gdx.graphics.GL10 gl10, com.badlogic.gdx.graphics.GLU glu) {
		if (!view.engine.isPaused) {
			gl10.glClearColor(background[0], background[1], background[2], background[3]); 	//Background
			// Update world.
			view.engine.myWorld.update();
			
			gl10.glEnable(GL10.GL_LIGHTING);
            gl10.glEnable(GL10.GL_LIGHT0);
            gl10.glEnable(GL10.GL_COLOR_MATERIAL);
            gl10.glEnable(GL10.GL_TEXTURE_2D);
			lightPositionBuffer.clear();
			lightPositionBuffer.put(lightPosition);
			lightPositionBuffer.position(0);
			gl10.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer);
			gl10.glEnable(GL10.GL_LIGHT0);
			
			gl10.glMatrixMode(GL10.GL_MODELVIEW); // Select The Modelview Matrix
			gl10.glLoadIdentity(); // Reset The Modelview Matrix
		
			
			glu.gluLookAt(gl10,
					new Float(75f), 50f,
					new Float(300f),
					new Float(75f), 45f, new Float(-3f), 0f, 1f, 0f);
			gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			for (MyPhysicsActor actor : view.engine.actors) {
				actor.draw(gl10);
			} 
		}
	}

	float lightX = 0.1f;
	float lightY = 0.1f;
	float lightZ = 10f;
	float lightCenterX = 75f;
	float lightCenterY = 100f;
	float lightCenterZ = 0f;
	
	public void updateLightPosition(float x, float y, float z) {
		float v = ((x*lightX)+(y*lightY)+(z*lightZ))/((x*x)+(y*y)+(-z*-z)); 
		lightPosition[0] = (lightCenterX+(x*v));
		lightPosition[1] = (lightCenterY+(y*v));
		lightPosition[2] = (lightCenterZ+((-z*v)));
	}

	public void initGL(GL10 gl) {
		gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); // Select The Projection Matrix
		gl.glLoadIdentity(); // Reset The Projection Matrix

		Gdx.graphics.getGLU().gluPerspective(gl, 45.0f, (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight(), 0.1f, 1000.0f);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lightSpecularBuffer);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbientBuffer); // Setup
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuseBuffer); // Setup
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPositionBuffer); // Position
		gl.glEnable(GL10.GL_LIGHTING);
		
		gl.glDisable(GL10.GL_DITHER);				//Disable dithering ( NEW )
		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		
		//gl.glEnable(GL10.GL_NORMALIZE);				// Normalaaais
		
		//gl.glCullFace(GL10.GL_BACK);                // Set Culling Face To Back Face
		gl.glDisable(GL10.GL_CULL_FACE);             // Enable Culling
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
	}

	
	public void setBackground(float red, float green, float blue, float alpha) {
		background[0]=red;
		background[1]=green;
		background[2]=blue;
		background[3]=alpha;
	}
}
