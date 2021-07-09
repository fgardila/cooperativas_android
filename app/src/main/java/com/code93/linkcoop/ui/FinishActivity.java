package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.code93.linkcoop.R;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(FinishActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Espere unos segundos", Toast.LENGTH_SHORT).show();
    }
}