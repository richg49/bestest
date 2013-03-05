package hu.promarkvf.besttest;

import hu.promarkvf.besttest.SimpleGestureFilter.SimpleGestureListener;

import java.io.IOException;
import java.util.List;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.doubleclick.DfpAdView;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;

public class NevKepActivity extends Activity implements SimpleGestureListener, AdListener {
	private SimpleGestureFilter detector;
	List<Kerdes_hiv> kerdes_hivek = null;
	ImageView[] ivk;
	TextView tvKerdes;
	TextView tvsorsz;
	TextView tveredmeny;
	int maxKerdes;
	int aktKerdes;
	int osszvalasz;
	int jovalasz;
	String valasz;
	private AudioManager audio;

	/** Samples that this project supports. */
	private enum DfpSample {
		STANDARD_BANNER, MULTIPLE_AD_SIZES, APP_EVENTS
	}

	/** The Sample to run. Change this value to run a different sample. */
	private static final DfpSample SAMPLE_TO_RUN = DfpSample.STANDARD_BANNER;

	/** The log tag. */
	private static final String LOG_TAG = "BannerSample";

	/** Ad Unit Constants. */
	private static final String AD_UNIT_STANDARD_BANNER = "/6253334/dfp_example_ad/banner";
	// private static final String AD_UNIT_MULTIPLE_SIZES =
	// "/6253334/dfp_example_ad/multisize";
	// private static final String AD_UNIT_APP_EVENTS =
	// "/6253334/dfp_example_ad/appevents";

	/** The view to show the ad. */
	private DfpAdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nev_kep);
		audio = (AudioManager) getSystemService(NevKepActivity.AUDIO_SERVICE);

		detector = new SimpleGestureFilter(this, this);

		tvsorsz = (TextView) findViewById(R.id.textView_sorsz);
		tveredmeny = (TextView) findViewById(R.id.textView_eredmeny);
		tvKerdes = (TextView) findViewById(R.id.tv_kerdes);
		ivk = new ImageView[4];
		ivk[0] = (ImageView) findViewById(R.id.imageView_k1);
		ivk[1] = (ImageView) findViewById(R.id.imageView_k2);
		ivk[2] = (ImageView) findViewById(R.id.imageView_k3);
		ivk[3] = (ImageView) findViewById(R.id.imageView_k4);

		final Button btnjobb = (Button) findViewById(R.id.db_buttonjobb);
		final Button btnbal = (Button) findViewById(R.id.db_buttonball);

		tveredmeny.setText(getString(R.string.valaszdb) + String.valueOf(jovalasz) + " / " + String.valueOf(osszvalasz));

		// --- egy kérdés jobbra
		btnjobb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				jobra();
			}
		});

		// --- egy kérdés balra
		btnbal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				balra();
			}
		});

		DatabaseHelper dbhelper = new DatabaseHelper(this);
		kerdes_hivek = dbhelper.Kerdes_hiv_tolt(MainActivity.kerdespersorozat, 0);
		maxKerdes = kerdes_hivek.size() - 1;
		tvsorsz.setText("1 / " + String.valueOf(maxKerdes + 1));
		aktKerdes = 0;
		if ( maxKerdes > 3 ) {
			Kerdes_hiv_megjelenit(0);
		}
		else {
			Toast.makeText(this, getString(R.string.keveskerdes), Toast.LENGTH_SHORT).show();
		}
		// The DfpAdView initialization depends on the type of example you wish
		// to run. Change the
		// value of SAMPLE_TO_RUN to run a different sample.
		switch ( SAMPLE_TO_RUN ) {
		case STANDARD_BANNER:
			// Example 1. Using sample DFP banner.
			// setContentView(R.layout.activity_nev_kep);
			adView = new DfpAdView(this, AdSize.BANNER, AD_UNIT_STANDARD_BANNER);
			break;
		default:
			break;
		}

		/** The implementation below is the same for all examples. */
		// Set the AdListener to listen for standard ad events.
		adView.setAdListener(this);

		// Lookup your LinearLayout assuming it’s been given the attribute
		// android:id="@+id/mainLayout".
		TableRow layout = (TableRow) findViewById(R.id.NevKepad);

		// Add the adView to it.
		layout.addView(adView);

		// Initiate an request to load the AdView with an ad.
		adView.loadAd(new AdRequest());
	}

	/*
	 * Megjeleníti az aktuális kérdést
	 */
	protected void Kerdes_hiv_megjelenit(int kerdessorsz) {
		int kerdesID = 0;
		int j = 1;
		/*
		 * try { Thread.sleep(3000); } catch ( InterruptedException e1 ) { //
		 */
		final DatabaseHelper dbHelper = new DatabaseHelper(this);
		// melyik válasz legyen a jó
		int keppos = (int) Math.floor( ( Math.random() * 100 ) % 3);
		for ( int i = 0; i <= 3; i++ ) {
			if ( i == keppos ) {
				kerdesID = kerdes_hivek.get(kerdessorsz).getKerdes_id(0);
				ivk[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						jovalasz++;
						osszvalasz++;
						tveredmeny.setText(getString(R.string.valaszdb) + String.valueOf(jovalasz) + " / " + String.valueOf(osszvalasz));
						Toast.makeText(NevKepActivity.this, getString(R.string.jovalasz), Toast.LENGTH_SHORT).show();

						Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_picture);
						arg0.startAnimation(rotate);
						try {
							AssetFileDescriptor afd = getAssets().openFd("NFF-choice-good.wav");
							MediaPlayer player = new MediaPlayer();
							player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							player.prepare();
							player.start();
							if ( MainActivity.autostepp ) {
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
									public void run() {
										jobra();
									}
								}, MainActivity.autostepptime * 1000);
							}
						}
						catch ( IllegalArgumentException e ) {
							e.printStackTrace();
						}
						catch ( IllegalStateException e ) {
							e.printStackTrace();
						}
						catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				});
			}
			else {
				kerdesID = kerdes_hivek.get(kerdessorsz).getKerdes_id(j);
				ivk[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						osszvalasz++;
						tveredmeny.setText(getString(R.string.valaszdb) + String.valueOf(jovalasz) + " / " + String.valueOf(osszvalasz));
						Toast.makeText(NevKepActivity.this, getString(R.string.rosszvalasz), Toast.LENGTH_SHORT).show();
						/*
						 * AlertDialog.Builder alertDialogBuilder = new
						 * AlertDialog.Builder(NevKepActivity.this);
						 * 
						 * alertDialogBuilder.setTitle("Your Title");
						 * alertDialogBuilder.setMessage("Click yes to exit!");
						 * alertDialogBuilder.setCancelable(false);
						 * alertDialogBuilder.setPositiveButton("Yes", new
						 * DialogInterface.OnClickListener() { public void
						 * onClick(DialogInterface dialog, int id) {
						 * NevKepActivity.this.finish(); } });
						 * alertDialogBuilder.setNegativeButton("No", new
						 * DialogInterface.OnClickListener() { public void
						 * onClick(DialogInterface dialog, int id) {
						 * dialog.cancel(); } }); AlertDialog alertDialog =
						 * alertDialogBuilder.create(); alertDialog.show();
						 */
						Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrat_picture);
						arg0.startAnimation(rotate);
						try {
							AssetFileDescriptor afd = getAssets().openFd("NFF-no-go.wav");
							MediaPlayer player = new MediaPlayer();
							player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							player.prepare();
							player.start();
						}
						catch ( IllegalArgumentException e ) {
							e.printStackTrace();
						}
						catch ( IllegalStateException e ) {
							e.printStackTrace();
						}
						catch ( IOException e ) {
							e.printStackTrace();
						}
					}
				});

				j++;
			}
			ivk[i].setImageBitmap(dbHelper.getBitmapKerdesId(kerdesID));
		}
		String kerd_1 = dbHelper.getKerdesId(kerdes_hivek.get(kerdessorsz).getKerdes_id(0));
		tvKerdes.setText(kerd_1);
		tvsorsz.setText(getString(R.string.kerdesdb) + String.valueOf(kerdessorsz + 1) + " / " + String.valueOf(maxKerdes + 1));
	}

	private void balra() {
		if ( aktKerdes > 0 ) {
			aktKerdes--;
			Kerdes_hiv_megjelenit(aktKerdes);
		}
		else {
			Toast.makeText(NevKepActivity.this, getString(R.string.nincstobb), Toast.LENGTH_SHORT).show();
		}
	}

	private void jobra() {
		if ( aktKerdes < maxKerdes ) {
			aktKerdes++;
			Kerdes_hiv_megjelenit(aktKerdes);
		}
		else {
			Toast.makeText(NevKepActivity.this, getString(R.string.nincstobb), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		switch ( direction ) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			balra();
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			jobra();
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;
		case SimpleGestureFilter.SWIPE_UP:
			break;
		}
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch ( keyCode ) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		case KeyEvent.KEYCODE_BACK:
			onBackPressed();
			return true;
		default:
			return false;
		}
	}

	/**
	 * Called when a DFP creative invokes an app event.
	 * 
	 * The app event creative is set up to send color=red when the ad is loaded,
	 * color=green when the ad is clicked, and color=blue after 5 seconds. This
	 * example will listen for these events to change the app's background
	 * color.
	 */
	// @Override
	public void onAppEvent(Ad ad, String name, String info) {
		String message = String.format("Received app event (%s, %s)", name, info);
		Log.d(LOG_TAG, message);
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		if ( "color".equals(name) ) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.DbToolsLinearLayout);
			if ( "red".equals(info) ) {
				layout.setBackgroundColor(Color.RED);
			}
			else if ( "green".equals(info) ) {
				layout.setBackgroundColor(Color.GREEN);
			}
			else if ( "blue".equals(info) ) {
				layout.setBackgroundColor(Color.BLUE);
			}
		}
	}

	/** Called when an ad is received. */
	@Override
	public void onReceiveAd(Ad ad) {
		Log.d(LOG_TAG, "onReceiveAd");
	}

	/** Called when an ad was not received. */
	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
		String message = String.format("onFailedToReceiveAd (%s)", error);
		Log.d(LOG_TAG, message);
	}

	/**
	 * Called when an Activity is created in front of the app (e.g. an
	 * interstitial is shown, or an ad is clicked and launches a new Activity).
	 */
	@Override
	public void onPresentScreen(Ad ad) {
		Log.d(LOG_TAG, "onPresentScreen");
	}

	/** Called when an ad is exited and about to return to the application. */
	@Override
	public void onDismissScreen(Ad ad) {
		Log.d(LOG_TAG, "onDismissScreen");
	}

	/**
	 * Called when an ad is clicked and going to start a new Activity that will
	 * leave the application (e.g. breaking out to the Browser or Maps
	 * application).
	 */
	@Override
	public void onLeaveApplication(Ad ad) {
		Log.d(LOG_TAG, "onLeaveApplication");
	}

}
