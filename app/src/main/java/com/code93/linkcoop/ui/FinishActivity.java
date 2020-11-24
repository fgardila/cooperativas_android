package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.code93.linkcoop.R;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.Transaccion;

public class FinishActivity extends AppCompatActivity {

    Cooperativa cooperativa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);


        ImageView imgToolLogoCoop = findViewById(R.id.imgToolLogoCoop);

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cooperativa = (Cooperativa)getIntent().getSerializableExtra("Cooperativa");
            if (cooperativa != null) {
                if (cooperativa.getUrlImgCoop() != null ) {
                    if (!cooperativa.getUrlImgCoop().trim().isEmpty()) {
                        //Picasso.get().load(cooperativa.getUrlImgCoop()).into(imgToolLogoCoop);
                    } else {
                        if (cooperativa.getIdDrawable() != 0) {
                            imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                        }
                    }
                } else {
                    if (cooperativa.getIdDrawable() != 0) {
                        imgToolLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                    }
                }
            }
        }*/


        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(FinishActivity.this, MainActivity.class);
            startActivity(intent);
        }, 2000);
    }
}