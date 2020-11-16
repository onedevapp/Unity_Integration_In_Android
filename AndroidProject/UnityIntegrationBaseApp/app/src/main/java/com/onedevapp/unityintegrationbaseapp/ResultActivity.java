package com.onedevapp.unityintegrationbaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("score", 0);

        TextView displayScore = (TextView) findViewById(R.id.tvScore);

        displayScore.setText(String.valueOf(score));
    }

    public void onHomeClick(View view) {

        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}