package com.rookiedev.aeroplanechess.app;

import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.view.CardFragment;


public class HomeActivity extends AppCompatActivity implements CardFragment.OnCardClickListener {
    // if user start a new game
    public static boolean isPlayed = false;

    private int page = 0;

    // if there was a game cached already
    private boolean isCached = false;
    private Context mContext = this;

    private FragmentManager fragmentManager;
    private CardFragment cardNewGame, cardResume, cardTwoPlayers, cardFourPlayers, cardRedVBlue, cardYellowVGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readPref();

        cardNewGame = CardFragment.newInstance(CardFragment.NEW_GAME);
        cardResume = CardFragment.newInstance(CardFragment.RESUME_GAME);
        cardTwoPlayers = CardFragment.newInstance(CardFragment.TWO_PLAYERS);
        cardFourPlayers = CardFragment.newInstance(CardFragment.FOUR_PLAYERS);
        cardRedVBlue = CardFragment.newInstance(CardFragment.RED_VS_BLUE);
        cardYellowVGreen = CardFragment.newInstance(CardFragment.YELLOW_VS_GREEN);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        if (isCached) {
            transaction.replace(R.id.frame1, cardResume);
            transaction.replace(R.id.frame2, cardNewGame);
        } else {
            transaction.replace(R.id.frame1, cardNewGame);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction;
        if (page == 1) {
            if (isCached) {
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_reverse, R.anim.exit_reverse);
                transaction.replace(R.id.frame1, cardResume);
                transaction.replace(R.id.frame2, cardNewGame);
                transaction.commit();
            } else {
                transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_reverse, R.anim.exit_reverse);
                transaction.replace(R.id.frame1, cardNewGame);
                transaction.remove(cardFourPlayers);
                transaction.commit();
            }
            page = 0;
        } else if (page == 2) {
            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_reverse, R.anim.exit_reverse);
            transaction.replace(R.id.frame1, cardTwoPlayers);
            transaction.replace(R.id.frame2, cardFourPlayers);
            transaction.commit();
            page = 1;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlayed) {
            // homeView.resetView();
        }
        readPref();
    }

    private void readPref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        isCached = prefs.getString(Constants.ISCACHED_PREF, "false").equals("true");
    }


    @Override
    public void onCardClicked(int cardType) {
        FragmentTransaction transaction;
        Intent intent = new Intent();
        switch (cardType) {
        case CardFragment.NEW_GAME:
            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
            transaction.replace(R.id.frame1, cardTwoPlayers);
            transaction.replace(R.id.frame2, cardFourPlayers);
            transaction.commit();
            page = 1;
            break;
        case CardFragment.RESUME_GAME:
            intent.setClass(mContext, PlayActivity.class);
            intent.putExtra(Constants.ISCACHED_PREF, "true");
            intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.FOURPLAYER));
            mContext.startActivity(intent);
            page = 0;
            break;
        case CardFragment.TWO_PLAYERS:
            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit);
            transaction.replace(R.id.frame1, cardRedVBlue);
            transaction.replace(R.id.frame2, cardYellowVGreen);
            transaction.commit();
            page = 2;
            break;
        case CardFragment.FOUR_PLAYERS:
            intent.setClass(mContext, PlayActivity.class);
            intent.putExtra(Constants.ISCACHED_PREF, "false");
            intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.FOURPLAYER));
            mContext.startActivity(intent);
            page = 1;
            break;
        case CardFragment.RED_VS_BLUE:
            intent.setClass(mContext, PlayActivity.class);
            intent.putExtra(Constants.ISCACHED_PREF, "false");
            intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.REDvsBLUE));
            mContext.startActivity(intent);
            break;
        case CardFragment.YELLOW_VS_GREEN:
            intent.setClass(mContext, PlayActivity.class);
            intent.putExtra(Constants.ISCACHED_PREF, "false");
            intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.YELLOWvsGREEN));
            mContext.startActivity(intent);
            break;
        }
    }
}
