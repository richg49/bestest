package hu.promarkvf.besttest;

import java.io.ByteArrayInputStream;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
//			Bitmap nbm = Bitmap.createBitmap(600, 600, null);
			Bitmap nbm = Bitmap.createBitmap(theImage, 495, 1200, 600, 600);
//			ivPhoto.setImageBitmap(theImage);
			ivPhoto.setImageBitmap(nbm);
			mCamera.stopPreview();
			preview.removeView(mPreview);
			ivPhoto.setVisibility(View.VISIBLE);
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mCamera = Camera.open();

		//Camera.Parameters parameters = mCamera.getParameters();
		//parameters.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
		//mCamera.setParameters(parameters);
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
//		captureButton.setVisibility(View.GONE);

		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);

		RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseLayout);
		AugmentedView av = new AugmentedView(this);
		baseLayout.addView(av, new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	@Override
	protected void onStop() {
		if (mCamera != null) {
			// NE FELEJTS�K EL!
			mCamera.release();
		}
		super.onStop();
	}
}
