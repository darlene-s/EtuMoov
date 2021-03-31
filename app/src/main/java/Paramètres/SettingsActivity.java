package Paramètres;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.etumoov.Accueil.AuthentificationMain;
import com.example.etumoov.Profil.ProfilActivity;
import com.example.etumoov.R;
import com.google.firebase.auth.FirebaseAuth;
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
        private androidx.preference.Preference scanQRCode, decoPreference, retour;
        private androidx.preference.EditTextPreference URLSYNC;
        private DataBaseHelper db;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            db = new DataBaseHelper(this.getContext());
            scanQRCode = findPreference("SCANQRCODE");
            URLSYNC = findPreference("URLSYNC");
            decoPreference = findPreference("DECO");
            retour = findPreference("retour");
            retour.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(preference.getContext(), ProfilActivity.class));
                    return true;
                }
            });

            decoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseAuth.getInstance().signOut(); // Déconnexion de l'utilisateur
                        db.deleteDataUser();
                        startActivity(new Intent(preference.getContext(), AuthentificationMain.class));
                        db.close();
                    }
                    else {
                        Toast.makeText(preference.getContext(), "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    return true;
                }
            });

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