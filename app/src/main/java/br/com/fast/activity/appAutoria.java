package br.com.fast.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import br.com.fast.R;

public class appAutoria extends AppCompatActivity {
    ImageView logoutfpr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_autoria);
        logoutfpr = findViewById(R.id.imageViewUTFPR);
        logoutfpr.setImageResource(R.drawable.logoutfpr);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Autoria do APP");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                setResult(Activity.RESULT_CANCELED);
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}