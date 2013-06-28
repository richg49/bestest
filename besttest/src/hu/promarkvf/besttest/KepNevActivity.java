package hu.promarkvf.besttest;

import hu.promarkvf.besttest.SimpleGestureFilter.SimpleGestureListener;

import java.io.IOException;
import java.util.List;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class KepNevActivity extends Activity implements SimpleGestureListener {
	private SimpleGestureFilter detector;
	List<Kerdes_hiv> kerdes_hivek = null;
	ImageView ivk;
	TextView[] tvKerdes;
	TextView tvsorsz;
	TextView tveredmeny;
	int maxKerdes;
	int aktKerdes;
	int osszvalasz;
	int jovalasz;
	String valasz;
	private AudioManager audio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		setContentView(R.layout.activity_kep_nev);
		detector = new SimpleGestureFilter(this, this);

		tvsorsz = (TextView) findViewById(R.id.textView_kn_sorsz);
		tveredmeny = (TextView) findViewById(R.id.textView_kn_eredmeny);
		ivk = (ImageView) findViewById(R.id.im_kerdes);
		tvKerdes = new TextView[4];
		tvKerdes[0] = (TextView) findViewById(R.id.textView_k1);
		tvKerdes[1] = (TextView) findViewById(R.id.textView_k2);
		tvKerdes[2] = (TextView) findViewById(R.id.textView_k3);
		tvKerdes[3] = (TextView) findViewById(R.id.textView_k4);

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
		kerdes_hivek = dbhelper.Kerdes_hiv_tolt(20, 0);
		maxKerdes = kerdes_hivek.size() - 1;
		tvsorsz.setText("1 / " + String.valueOf(maxKerdes + 1));
		aktKerdes = 0;
		if ( maxKerdes > 3 ) {
			Kerdes_hiv_megjelenit(0);
		}
		else {
			Toast.makeText(this, getString(R.string.keveskerdes), Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Megjeleníti az aktuális kérdést
	 */
	protected void Kerdes_hiv_megjelenit(int kerdessorsz) {
		int kerdesID = 0;
		int j = 1;
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		// melyik válasz legyen a jó
		int keppos = (int) Math.floor( ( Math.random() * 100 ) % 3);
		for ( int i = 0; i <= 3; i++ ) {
			if ( i == keppos ) {
				kerdesID = kerdes_hivek.get(kerdessorsz).getKerdes_id(0);
				tvKerdes[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						jovalasz++;
						osszvalasz++;
						tveredmeny.setText(getString(R.string.valaszdb) + String.valueOf(jovalasz) + " / " + String.valueOf(osszvalasz));
						Toast.makeText(KepNevActivity.this, getString(R.string.jovalasz), Toast.LENGTH_SHORT).show();
						Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrat_picture);
						arg0.startAnimation(rotate);
						arg0.setBackgroundColor(Color.GREEN);
						try {
							AssetFileDescriptor afd = getAssets().openFd("NFF-choice-good.wav");
							MediaPlayer player = new MediaPlayer();
							player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							player.prepare();
							player.start();
							if ( MainActivity.autostepp ) {
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
									@Override
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
				tvKerdes[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						osszvalasz++;
						tveredmeny.setText(getString(R.string.valaszdb) + String.valueOf(jovalasz) + " / " + String.valueOf(osszvalasz));
						Toast.makeText(KepNevActivity.this, getString(R.string.rosszvalasz), Toast.LENGTH_SHORT).show();
						Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.vibrat_picture);
						arg0.startAnimation(rotate);
						arg0.setBackgroundColor(Color.RED);
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
			tvKerdes[i].setText(dbHelper.getKerdesId(kerdesID));
			tvKerdes[i].setBackgroundColor(Color.WHITE);

		}
		ivk.setImageBitmap(dbHelper.getBitmapKerdesId(kerdes_hivek.get(kerdessorsz).getKerdes_id(0)));
		tvsorsz.setText(getString(R.string.kerdesdb) + String.valueOf(kerdessorsz + 1) + " / " + String.valueOf(maxKerdes + 1));
	}

	private void balra() {
		if ( aktKerdes > 0 ) {
			aktKerdes--;
			Kerdes_hiv_megjelenit(aktKerdes);
		}
		else {
			Toast.makeText(KepNevActivity.this, getString(R.string.nincstobb), Toast.LENGTH_SHORT).show();
		}
	}

	private void jobra() {
		if ( aktKerdes < maxKerdes ) {
			aktKerdes++;
			Kerdes_hiv_megjelenit(aktKerdes);
		}
		else {
			Toast.makeText(KepNevActivity.this, getString(R.string.nincstobb), Toast.LENGTH_SHORT).show();
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
}
