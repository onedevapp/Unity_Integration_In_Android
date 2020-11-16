package com.onedevapp.unityintegrationbaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlayGameAClick(View view) {
        Intent intent = new Intent(this, TapGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("highScore", 50);
        intent.putExtra("gameScene", "SceneA");
        startActivity(intent);
    }

    public void onPlayGameBClick(View view) {
        Intent intent = new Intent(this, TapGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("highScore", 0);
        intent.putExtra("gameScene", "SceneB");
        startActivity(intent);
    }

    public void onPlayGameCClick(View view) {
        Intent intent = new Intent(this, UnityFragmentActivity.class);
        intent.putExtra("highScore", 0);
        intent.putExtra("gameScene", "SceneC");
        startActivity(intent);
    }
}