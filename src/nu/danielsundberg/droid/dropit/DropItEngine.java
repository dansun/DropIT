package nu.danielsundberg.droid.dropit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import nu.danielsundberg.droid.dropit.physics.MyPhysicsActor;
import nu.danielsundberg.droid.dropit.physics.MyPhysicsWorld;
import nu.danielsundberg.droid.dropit.view.MyGlView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public final class DropItEngine implements ApplicationListener, SensorEventListener {

	private static final String LOG_TAG = DropIt.class.getSimpleName();
	public MyGlView myView;
	public MyPhysicsWorld myWorld;
	public List<PlaceHolderObject> toBeAdded = new CopyOnWriteArrayList<PlaceHolderObject>();
	public List<MyPhysicsActor> actors = new CopyOnWriteArrayList<MyPhysicsActor>();
	private static final float ROWHEIGHT = 6f;
	private static final float BOXWIDTH = 6f;
	private static final String FILENAME = "downloadedtexture.png";
	private static final float MAX_IMAGE_SIZE = 32;

	public Sound bounce;
	public Sound bounce2;
	public Sound bounce3;
	public boolean isPaused = false;
	public SensorManager sensorManager = null;
	private Context context;
	boolean inited = false;
	private String urlToLoad = "";
	
	
	public DropItEngine(Context context) {
		this.context = context;
		this.sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		registerSensorlistners();

		myView = new MyGlView(context);
		myView.setParent(this);
		

		myWorld = new MyPhysicsWorld();
		myWorld.setParent(this);
		myWorld.create();
	}

	@Override
	public void create() {
		myView.renderer.initGL(Gdx.app.getGraphics().getGL10());
		FileHandle fh = Gdx.files.internal("data/bounce.wav");
		bounce = Gdx.audio.newSound(fh);
		FileHandle fh2 = Gdx.files.internal("data/pingpongtwo.wav");
		bounce2 = Gdx.audio.newSound(fh2);
		FileHandle fh3 = Gdx.files.internal("data/pingpongtwo.wav");
		bounce3 = Gdx.audio.newSound(fh3);
		Log.d(LOG_TAG, "create");
		
	}

	@Override
	public void dispose() {
		Log.d(LOG_TAG, "dispose");
	}

	@Override
	public void pause() {
		isPaused = true;
		Log.d(LOG_TAG, "pause");
		unregisterSensorlistners();
	}

	@Override
	public void render() {
		if(inited) {
			myView.renderer.onDrawFrame(Gdx.app.getGraphics().getGL10(), Gdx.app.getGraphics().getGLU());	
		} else if(!inited&&!urlToLoad.equals("")) {
			init();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		myView.renderer.onSurfaceChanged(Gdx.app.getGraphics().getGL10(), width, height);	
		Log.d(LOG_TAG, "resize");
	}

	@Override
	public void resume() {
		isPaused = false;
		Log.d(LOG_TAG, "resume");
		registerSensorlistners();
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	public void registerSensorlistners() {
		// Register this class as a listener
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_GAME);
		

	}

	public void unregisterSensorlistners() {
		sensorManager.unregisterListener(this);
	}

	// This method will update the UI on new sensor events
	public void onSensorChanged(SensorEvent sensorEvent) {
		synchronized (this) {
			if (sensorEvent.sensor.getType() == Sensor.TYPE_GRAVITY) {
				myWorld.updateGravity(-sensorEvent.values[0],
						-sensorEvent.values[1]);
			}
		}
	}

	public Texture downloadFile(String fileUrl) {
		try {
			InputStream is = null; 
			HttpURLConnection conn = null;
			if(fileUrl.contains("http://")) {
				URL myFileUrl = null;
				try {
					myFileUrl = new URL(fileUrl);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				is = conn.getInputStream();
			} else if (fileUrl.contains("file://")||fileUrl.contains("content://")) {
				Uri uri = Uri.parse(fileUrl);
				is = context.getContentResolver().openInputStream(uri);
			}
				
			InputStreamReader reader = new InputStreamReader(is);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			
			float originalWidth = bitmap.getWidth();
			float originalHeight = bitmap.getHeight();
			float ratio; 
			float newWidth;
			float newHeight;
			
			if(originalWidth>=originalHeight) {
				ratio = originalWidth/originalHeight;
				newWidth = MAX_IMAGE_SIZE;
				newHeight = newWidth/ratio;
			} else {
				ratio = originalHeight/originalWidth;
				newHeight = MAX_IMAGE_SIZE;
				newWidth = newHeight/ratio;
				
			}
			
			
			int finalWidth = (int) (Math.pow(2,
					Math.round(Math.log(newWidth) / Math.log(2))));
			int finalHeight = (int) (Math.pow(2,
					Math.round(Math.log(newHeight) / Math.log(2))));
			bitmap = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false);

			File tmp = new File(Environment.getExternalStorageDirectory(),
					FILENAME);
			OutputStream out = new FileOutputStream(tmp);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			reader.close();
			is.close();
			if(conn!=null) conn.disconnect();
			

			FileHandle fh = Gdx.files.absolute(tmp.getAbsolutePath());
			return new Texture(fh);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void init() {

		Texture t = downloadFile(urlToLoad );
		
		int nrRows = Math.round(t.getHeight() / (BOXWIDTH));
		int nrColumns = Math.round(t.getWidth() / (BOXWIDTH));
		int STARTX=50;
		int STARTY=50;
		for (int rowNr = 0; rowNr < nrRows ; rowNr++) {
			for (int position = 0; position < nrColumns; position++) {
				float u = position * (1f / nrColumns);
				float v = rowNr * (1f / nrRows);
				float u1 = (position + 1) * (1f / nrColumns);
				float v1 = (rowNr + 1) * (1f / nrRows);
				addTexturedBox(((position+1) * BOXWIDTH) + STARTX,
						((nrRows-rowNr) * ROWHEIGHT) + STARTY, t, u, v, u1, v1);
			}
		}
		inited = true;
		
	}

	private void addTexturedBox(float f, float g, Texture t, float h, float i, float j, float k) {
		toBeAdded.add(new PlaceHolderObject(f, g, t, h, i, j, k));
	}

	public class PlaceHolderObject {
		public float sx;
		public float sy;
		public float u1;
		public float u2;
		public float v1;
		public float v2;
		public boolean isRed;
		public boolean isTextured;
		public Texture texture;

		public PlaceHolderObject(float sx, float sy, boolean isRed) {
			this.sx = sx;
			this.sy = sy;
			this.isRed = isRed;
			isTextured = false;
		}

		public PlaceHolderObject(float sx, float sy, Texture t, float u1,
				float v1, float u2, float v2) {
			this.sx = sx;
			this.sy = sy;
			this.texture = t;
			this.u1 = u1;
			this.u2 = u2;
			this.v1 = v1;
			this.v2 = v2;
			isTextured = true;

		}

		public boolean isRed() {
			return isRed;
		}
	}

	public void setUrlToLoad(String value) {
		this.urlToLoad = value;
	}
}
