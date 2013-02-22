package hu.promarkvf.besttest;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyPreview extends SurfaceView implements SurfaceHolder.Callback {
  private SurfaceHolder mHolder;
  private Camera mCamera;
      
  /*private PreviewCallback pvc = new PreviewCallback() {
    @Override
	public void onPreviewFrame(byte[] data, Camera camera) {
      Log.d("CAM","Preview size: "+data.length);	
    }
  };*/
  
  public MyPreview(Context context, Camera camera) {
      super(context);
      mCamera = camera;
      mHolder = getHolder();
      mHolder.addCallback(this);
      mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  public void surfaceCreated(SurfaceHolder holder) {
      try {
          mCamera.setPreviewDisplay(holder);
          mCamera.startPreview();
      } catch (IOException e) {
          Log.d("CAM", "Failed to start preview: " + e.getMessage());
      }
  }

  public void surfaceDestroyed(SurfaceHolder holder) {
    // TODO: tov�bbi lehet�s�gek kezel�se az el�n�zeti k�p bez�r�d�sakor
  }

  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    /*if (mHolder.getSurface() == null) {
      return;
    }
    try {
      mCamera.stopPreview();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      mCamera.setPreviewDisplay(mHolder);
      //mCamera.setPreviewCallback(pvc);
      mCamera.startPreview();
    } catch (Exception e) {
      Log.d("CAM", "Failed to start preview: " + e.getMessage());
    }*/
  }
}
