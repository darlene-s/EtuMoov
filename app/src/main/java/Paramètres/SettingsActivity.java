package Paramètres;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.etumoov.Accueil.AuthentificationMain;
import com.example.etumoov.MainActivity;
import com.example.etumoov.R;

import java.util.Locale;

import AffichageCours.Classes.CalendarJour;
import AffichageCours.Scanner.ScannerActivity;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private androidx.preference.Preference scanQRCode;
        private androidx.preference.EditTextPreference URLSYNC;
        private ListPreference list;
        private DataBaseHelper db;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            db = new DataBaseHelper(this.getContext());
            scanQRCode = findPreference("SCANQRCODE");
            URLSYNC = findPreference("URLSYNC");

            scanQRCode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(preference.getContext(), ScannerActivity.class));
                    return true;
                }
            });

            URLSYNC.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if(URLSYNC.getText() != null)
                        db.insertLien(newValue.toString());
                    Intent Calendar = new Intent(preference.getContext(), CalendarJour.class);
                    Calendar.putExtra("LienEDT", newValue.toString());
                    startActivity(Calendar);
                    return true;
                }
            });
        }
    }

}