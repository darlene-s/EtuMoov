package Meteo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.etumoov.R;

public class cityFinder extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editText=findViewById(R.id.searchCity);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity = editText.getText().toString();
                Intent intent = new Intent(cityFinder.this,MeteoActivity.class);
                intent.putExtra("City",newCity);
                startActivity(intent);
                return false;
            }
        });
    }
}