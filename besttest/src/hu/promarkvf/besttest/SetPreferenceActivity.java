package hu.promarkvf.besttest;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SetPreferenceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
	}

	public class PrefsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
	        addPreferencesFromResource(R.xml.setup);
		}

	}

}
