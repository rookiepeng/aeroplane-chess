package com.rookiedev.aeroplanechess.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.view.PlayView;

public class PlayActivity extends AppCompatActivity {
    private PlayView playView;
    private int player;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mRelativeLayout = findViewById(R.id.playLayout);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        // params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        Intent intent = getIntent();
        boolean isCached = intent.getStringExtra(Constants.ISCACHED_PREF).equals("true");
        if (!isCached) {
            player = Integer.parseInt(intent.getStringExtra(Constants.PLAYTYPE_PREF));
            if (player == Constants.YELLOWvsGREEN) {
                playView = new PlayView(this, player, Constants.YELLOW, false);
                // playView.setParameter(player, Constants.YELLOW);
            } else {
                playView = new PlayView(this, player, Constants.RED, false);
                // playView.setParameter(player, Constants.RED);
            }
        } else {
            playView = new PlayView(this, 0, 0, true);
            // playView.setResume();
        }
        mRelativeLayout.addView(playView, params);
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
}
