package hu.promarkvf.besttest;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MyPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private Context cont;

	/*
	 * private PreviewCallback pvc = new PreviewCallback() {
	 * 
	 * @Override public void onPreviewFrame(byte[] data, Camera camera) {
	 * Log.d("CAM","Preview size: "+data.length); } };
	 */

	public MyPreview(Context context, Camera camera) {
		super(context);
		cont = context;
		mCamera = camera;

		int rotation = ( (WindowManager) cont.getSystemService(Context.WINDOW_SERVICE) ).getDefaultDisplay().getRotation();
		int degrees = 0;
		switch ( rotation ) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		}
		catch ( IOException e ) {
			Log.d("CAM", "Failed to start preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO: tov�bbi lehet�s�gek kezel�se az el�n�zeti k�p bez�r�d�sakor
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Camera.Parameters parameters = mCamera.getParameters();
		Display display = ( (WindowManager) cont.getSystemService(Context.WINDOW_SERVICE) ).getDefaultDisplay();

		int height = display.getHeight();
		int width = display.getWidth();
		if ( display.getRotation() == Surface.ROTATION_0 ) {
			parameters.setPreviewSize(height, width);
			mCamera.setDisplayOrientation(90);
		}

		if ( display.getRotation() == Surface.ROTATION_90 ) {
			parameters.setPreviewSize(width, height);
		}

		if ( display.getRotation() == Surface.ROTATION_180 ) {
			parameters.setPreviewSize(height, width);
		}

		if ( display.getRotation() == Surface.ROTATION_270 ) {
			parameters.setPreviewSize(width, height);
			mCamera.setDisplayOrientation(180);
		}

//		mCamera.setParameters(parameters);
	}
}
