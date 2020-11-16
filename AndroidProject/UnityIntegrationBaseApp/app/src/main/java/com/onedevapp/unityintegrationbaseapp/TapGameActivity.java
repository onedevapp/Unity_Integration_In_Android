package com.onedevapp.unityintegrationbaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.OneDevApp.TapGame.OverrideUnityActivity;

public class TapGameActivity  extends OverrideUnityActivity {

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        setIntent(intent);
    }

    void handleIntent(Intent intent) {
        if(intent == null || intent.getExtras() == null) return;

        if(intent.getExtras().containsKey("doQuit"))
            if(mUnityPlayer != null) {
                finish();
            }
    }

    @Override
    protected void onGameFinish(int currentScore) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("score", currentScore);
        startActivity(intent);
        finishAffinity();
    }

    @Override public void onUnityPlayerUnloaded() {
        onGameFinish(0);
    }
}