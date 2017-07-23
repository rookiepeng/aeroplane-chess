package com.rookiedev.aeroplanechess.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.view.PlayView;

public class PlayActivity extends AppCompatActivity {
    private PlayView playView;
    private int player;
    private InterstitialAd interstitial;
    private AdRequest adRequest;
    private boolean isPremium=false;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //getSupportActionBar().hide();

        mRelativeLayout=(RelativeLayout) findViewById(R.id.playLayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        //params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        Intent intent = getIntent();
        boolean isCached = intent.getStringExtra(Constants.ISCACHED_PREF).equals("true");
        if (!isCached) {
            player = Integer.parseInt(intent
                    .getStringExtra(Constants.PLAYTYPE_PREF));
            if (player == Constants.YELLOWvsGREEN) {
                playView = new PlayView(this,player, Constants.YELLOW,false);
                //playView.setParameter(player, Constants.YELLOW);
            } else {
                playView = new PlayView(this,player, Constants.RED,false);
                //playView.setParameter(player, Constants.RED);
            }
        } else {
            playView = new PlayView(this,0, 0,true);
            //playView.setResume();
        }
        mRelativeLayout.addView(playView,params);

        // Create ad request.
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("015d172c791c0215")
                .addTestDevice("04afa117002e7ebc")
                .build();
        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-9759692679007522/2556917309");
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                displayInterstitial();
            }
        });

        checkPremium();
        if(!isPremium) {
            // Begin loading your interstitial.
            interstitial.loadAd(adRequest);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        playView.savePref();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void checkPremium() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME,
                MODE_PRIVATE);
        isPremium=prefs.getString(Constants.PREMIUM, "false").equals("true");
    }
}
