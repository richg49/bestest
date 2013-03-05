package hu.promarkvf.besttest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.ads.*;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.doubleclick.*;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */

public class MainActivity extends Activity implements AdListener, OnClickListener {
	private static final int NEW_USER_ACTIVITY_ID = 1;
	private static final int MOD_USER_ACTIVITY_ID = 2;
	private static final int SEL_USER_ACTIVITY_ID = 3;

	public static int image_h;
	public static int image_w;
	public static int kerdespersorozat;
	public static boolean autostepp;
	public static int autostepptime;
	public static User sel_user;

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

		setContentView(R.layout.activity_main);

		loadPref();

		switch ( SAMPLE_TO_RUN ) {
		case STANDARD_BANNER:
			// Example 1. Using sample DFP banner.
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
		TableRow layout = (TableRow) findViewById(R.id.mainad);

		// Add the adView to it.
		layout.addView(adView);

		// Initiate an request to load the AdView with an ad.
		adView.loadAd(new AdRequest());

		// Van már usernév?
		List<User> userList = new ArrayList<User>();
		DatabaseUser dbuser = new DatabaseUser(this);
		userList = dbuser.getAllUser();
		if ( sel_user == null ) {
			if ( userList.isEmpty() ) {
				User newuser = new User();
				Intent myIntent = new Intent();
				myIntent.setClass(MainActivity.this, AddUserActivity.class);
				ParcelableUser parcelableUser = new ParcelableUser(newuser);
				myIntent.putExtra("user", parcelableUser);
				startActivityForResult(myIntent, NEW_USER_ACTIVITY_ID);
			}
			else {
				if ( userList.size() > 1 ) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, UserSelActivity.class);
					startActivityForResult(intent, SEL_USER_ACTIVITY_ID);
				}
				else {
					sel_user = userList.get(0);
				}
			}
		}
	}

	Handler mHideHandler = new Handler();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadPref();

		switch ( requestCode ) {
		case NEW_USER_ACTIVITY_ID:
			if ( resultCode == RESULT_OK ) {
				ParcelableUser parcelableuser = (ParcelableUser) data.getParcelableExtra("user");
				User user = parcelableuser.getUser();
				DatabaseUser dbuser = new DatabaseUser(this);
				dbuser.AddUser(user);
				sel_user = user;
			}
			break;
		}
	}

	private void loadPref() {
		SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		String kerdespersorozatS = mySharedPreferences.getString("kerdespersorozat", "20");
		kerdespersorozat = Integer.parseInt(kerdespersorozatS);
		kerdespersorozat = kerdespersorozat == 0 ? 20 : kerdespersorozat;
		String image_hS = mySharedPreferences.getString("image_h", "130");
		image_h = Integer.parseInt(image_hS);
		image_h = image_h == 0 ? 130 : image_h;
		String image_wS = mySharedPreferences.getString("image_w", "130");
		image_w = Integer.parseInt(image_wS);
		image_w = image_w == 0 ? 130 : image_w;
		autostepp = mySharedPreferences.getBoolean("autostepp", Boolean.TRUE);
		// autostepp = Boolean.parseBoolean(autosteppS);
		if ( autostepp ) {
			String autostepptimeS = mySharedPreferences.getString("autostepptime", "0");
			autostepptime = Integer.parseInt(autostepptimeS);
			autostepptime = autostepptime == 0 ? 2 : autostepptime;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kep_nev, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingActivity.class);
		startActivityForResult(intent, 0);

		return true;
	}

	@Override
	public void onDestroy() {
		if ( adView != null ) {
			adView.destroy();
		}
		super.onDestroy();
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
		// --- Névhez képet inditas
		case R.id.m_button1:
			myIntent = new Intent();
			myIntent.setClass(MainActivity.this, NevKepActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);

			break;

		// --- Képhez nevet inditas
		case R.id.m_button2:
			myIntent = new Intent();
			myIntent.setClass(MainActivity.this, KepNevActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);

			break;
		// --- Beállítás inditas
		case R.id.m_button5:
			myIntent = new Intent();
			myIntent.setClass(MainActivity.this, BeallitasActivity.class);
			myIntent.putExtra("MyValue", "OK");
			startActivity(myIntent);
			break;

		// --- Kilépés
		case R.id.m_button6:
			finish();
			break;

		default:
			break;
		}
	}
}
