package com.rookiedev.aeroplanechess.app;

import android.app.AlertDialog;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rookiedev.aeroplanechess.app.billing.BillingConstants;
import com.rookiedev.aeroplanechess.app.billing.BillingManager;
import com.rookiedev.aeroplanechess.app.billing.BillingProvider;
import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.view.CardFragment;

import java.util.List;

import static com.rookiedev.aeroplanechess.app.billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;

public class HomeActivity extends AppCompatActivity implements BillingProvider, CardFragment.OnCardClickListener {
    // if user start a new game
    public static boolean isPlayed = false;
    // is the first to play the game
    private boolean isFirstRun;
    // Does the user have the premium upgrade?
    private boolean mIsPremium = false;

    private int page = 0;

    // if there was a game cached already
    private boolean isCached = false;

    // private HomeView homeView;
    private AdView adView;
    private Context mContext = this;

    private BillingManager mBillingManager;
    private final UpdateListener mUpdateListener = new UpdateListener();

    private FragmentManager fragmentManager;
    private CardFragment cardNewGame, cardResume, cardTwoPlayers, cardFourPlayers, cardRedVBlue, cardYellowVGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, "ca-app-pub-6523245757541965~8170734995");

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
        transaction.replace(R.id.frame1, cardResume);
        transaction.replace(R.id.frame2, cardNewGame);
        transaction.commit();

        adView = this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // All emulators
                .build();
        if (!mIsPremium) {
            adView.loadAd(adRequest);
        }

        // Create and initialize BillingManager which talks to BillingLibrary
        mBillingManager = new BillingManager(this, mUpdateListener);

        if (isFirstRun) {
            alertDialog();
            isFirstRun = false;
            savePref();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentTransaction transaction;
        if (page == 1) {
            transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter_reverse, R.anim.exit_reverse);
            transaction.replace(R.id.frame1, cardResume);
            transaction.replace(R.id.frame2, cardNewGame);
            transaction.commit();
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
        // homeView.setResume(isCached);
        // Note: We query purchases in onResume() to handle purchases completed while
        // the activity
        // is inactive. For example, this can happen if the activity is destroyed during
        // the
        // purchase flow. This ensures that when the activity is resumed it reflects the
        // user's
        // current purchases.
        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode() == BillingClient.BillingResponse.OK) {
            mBillingManager.queryPurchases();
        }
    }

    /**
     * We're being destroyed. It's important to dispose of the helper here!
     */
    @Override
    public void onDestroy() {
        if (mBillingManager != null) {
            mBillingManager.destroy();
        }
        super.onDestroy();
    }

    public void alertDialog() {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle(getString(R.string.Upgrade_title));
        alert.setMessage(getString(R.string.Upgrade_context));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.Upgrade),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBuyClicked();
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.Cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    /**
     * User clicked the "pro" button.
     */
    private void onBuyClicked() {
        if (mBillingManager != null
                && mBillingManager.getBillingClientResponseCode() > BILLING_MANAGER_NOT_INITIALIZED) {
            mBillingManager.initiatePurchaseFlow(BillingConstants.SKU_ADFREE, BillingClient.SkuType.INAPP);
        }
    }

    private void alert(String title, String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle(title);
        bld.setMessage(message);
        bld.setNeutralButton(getString(R.string.OK), null);
        // Log.d("TAG", "Showing alert dialog: " + message);
        bld.create().show();
    }

    private void readPref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        isFirstRun = prefs.getString(Constants.ISFIRSTRUN_PREF, "true").equals("true");
        isCached = prefs.getString(Constants.ISCACHED_PREF, "false").equals("true");
        mIsPremium = prefs.getString(Constants.PREMIUM, "false").equals("true");
    }

    private void savePref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (mIsPremium) {
            editor.putString(Constants.PREMIUM, "true");
        } else {
            editor.putString(Constants.PREMIUM, "false");
        }
        if (isFirstRun) {
            editor.putString(Constants.ISFIRSTRUN_PREF, "true");
        } else {
            editor.putString(Constants.ISFIRSTRUN_PREF, "false");
        }
        editor.apply();
    }

    @Override
    public BillingManager getBillingManager() {
        return null;
    }

    @Override
    public boolean isPremiumPurchased() {
        return false;
    }

    @Override
    public boolean isGoldMonthlySubscribed() {
        return false;
    }

    @Override
    public boolean isTankFull() {
        return false;
    }

    @Override
    public boolean isGoldYearlySubscribed() {
        return false;
    }

    @Override
    public void onCardClicked(int cardType) {
        FragmentTransaction transaction;
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
            page = 1;
            break;
        }
    }

    /**
     * Handler to billing updates
     */
    private class UpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() {

        }

        @Override
        public void onConsumeFinished(String token, @BillingClient.BillingResponse int result) {

        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {
            if (purchases.isEmpty()) {
                mIsPremium = false;
            }
            for (com.android.billingclient.api.Purchase purchase : purchases) {
                if (purchase.getSku().equals(BillingConstants.SKU_ADFREE)) {
                    alert(getString(R.string.pro_title), getString(R.string.pro_version));
                    // proButton.setVisibility(View.INVISIBLE); // hide buy button
                    adView.setVisibility(View.INVISIBLE); // hide AD
                    mIsPremium = true;
                    savePref();
                }
            }
        }
    }
}
