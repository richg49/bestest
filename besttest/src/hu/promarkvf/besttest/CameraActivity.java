package hu.promarkvf.besttest;

import java.io.ByteArrayInputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraActivity extends Activity {
	private Camera mCamera;
	private MyPreview mPreview;
	private ImageView ivPhoto;
	private FrameLayout preview;

	private PictureCallback mPicture = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("CAM", "Size: " + data.length);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(data);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			Bitmap newImage = Bitmap.createScaledBitmap(theImage, 300, 300, true);
/*			int winX = 300;
			int winY = 300;
			int maxX = theImage.getWidth();
			int maxY = theImage.getHeight();
			int x0 = (maxX - winX) / 2;
			int y0 = (maxY - winY) / 2;
			int x1 = x0 + winX;
			int y1 = y0 + winY;
			if (x0 > 0 && x1 > 0 && y0 > 0 && y1 > 0) {
				Bitmap nbm = Bitmap.createBitmap(theImage, 0, 0, winX, winY);
				ivPhoto.setImageBitmap(nbm);
			}
*/
//			DataPreferences.kep = newImage;
			ivPhoto.setImageBitmap(newImage);
			mCamera.stopPreview();
			preview.removeView(mPreview);
			ivPhoto.setVisibility(View.VISIBLE);
			Intent resultIntent = new Intent();
			resultIntent.putExtra("bitmap", newImage);
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mCamera = Camera.open();
//		DataPreferences.kep = null;
		
		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
	 	parameters.set("orientation", "portrait");
      	parameters.set("rotation", 90);
		mCamera.setParameters(parameters);

		mPreview = new MyPreview(this, mCamera);
		preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		final Button captureButton = (Button) findViewById(R.id.buttonPhoto);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPicture);
				captureButton.setEnabled(false);
			}
		});
		// captureButton.setVisibility(View.GONE);

		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

//		RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
//		AugmentedView av = new AugmentedView(this);
//		baseLayout.addView(av, new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onStop() {
		if (mCamera != null) {
			// NE FELEJTS�K EL!
			mCamera.release();
		}
		super.onStop();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
