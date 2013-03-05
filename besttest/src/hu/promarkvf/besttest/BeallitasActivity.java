package hu.promarkvf.besttest;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.doubleclick.DfpAdView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class BeallitasActivity extends Activity implements AdListener, OnClickListener {

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

		setContentView(R.layout.activity_beallitas);

		// The DfpAdView initialization depends on the type of example you wish
		// to run. Change the
		// value of SAMPLE_TO_RUN to run a different sample.
		switch ( SAMPLE_TO_RUN ) {
		case STANDARD_BANNER:
			// Example 1. Using sample DFP banner.
			setContentView(R.layout.activity_beallitas);
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
		TableRow layout = (TableRow) findViewById(R.id.beallitasad);

		// Add the adView to it.
		layout.addView(adView);

		// Initiate an request to load the AdView with an ad.
		adView.loadAd(new AdRequest());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
			LinearLayout layout = (LinearLayout) findViewById(R.id.mainLayout);
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

	@Override
	public void onClick(View v) {
		Intent myIntent = new Intent();
		switch ( v.getId() ) {
		// --- Adatbázis mentés visszatöltés
		case R.id.b_button1:
			myIntent = new Intent();
			myIntent.setClass(BeallitasActivity.this, DbToolsActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);

			break;

		// --- Kategóriák módosítása
		case R.id.b_button2:
			myIntent = new Intent();
			myIntent.setClass(BeallitasActivity.this, KategoriakActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);

			break;
		// --- Kérdések módosítása
		case R.id.b_button3:
			myIntent = new Intent();
			myIntent.setClass(BeallitasActivity.this, KerdesekActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);
			break;

		// --- lista
		case R.id.b_button4:
			break;

		// --- Személyek
		case R.id.b_button5:
			myIntent = new Intent();
			myIntent.setClass(BeallitasActivity.this, UsersActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);
			break;

		// --- Kilépés
		case R.id.b_button6:
			finish();
			break;

		default:
			break;
		}
	}
}
