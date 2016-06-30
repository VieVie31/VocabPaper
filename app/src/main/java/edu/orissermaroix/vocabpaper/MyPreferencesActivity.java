package edu.orissermaroix.vocabpaper;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Created by VieVie31 on 30/06/2016.
 */
public class MyPreferencesActivity extends PreferenceActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);

        //add a vlidator to the time_between_words preference so that only accepts numbers
        //Preference timeBetweenWords = getPreferenceScreen().findPreference("time_between_words");
        //add the validator
        //timeBetweenWords.setOnPreferenceChangeListener(numberCheckListener);
    }

    /**
     * Checks tha a preference is a valid numerical value
     */
    Preference.OnPreferenceChangeListener numberCheckListener = new OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            //check that the string is an integer
            if (newValue != null && newValue.toString().length() > 0
                    && newValue.toString().matches("\\d*"))
                return true;

            //if not create a message to the user
            Toast.makeText(MyPreferencesActivity.this, "Invalid Input",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    };
}
