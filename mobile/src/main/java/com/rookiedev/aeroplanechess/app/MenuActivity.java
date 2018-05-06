package com.rookiedev.aeroplanechess.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rookiedev.aeroplanechess.app.billing.BillingConstants;
import com.rookiedev.aeroplanechess.app.billing.BillingManager;
import com.rookiedev.aeroplanechess.app.billing.BillingProvider;
import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.view.HomeView;

import java.util.List;

import static com.rookiedev.aeroplanechess.app.billing.BillingManager.BILLING_MANAGER_NOT_INITIALIZED;

public class MenuActivity extends AppCompatActivity implements BillingProvider {
    // if user start a new game
    public static boolean isPlayed = false;
    // is the first to play the game
    private boolean isFirstRun;
    // Does the user have the premium upgrade?
    private boolean mIsPremium = false;

    // if there was a game cached already
    private boolean isCached = false;
    // views
    private HomeView homeView;
    private ImageButton proButton;
    private AdView adView;
    private Context mContext = this;

    private BillingManager mBillingManager;
    private final UpdateListener mUpdateListener = new UpdateListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        readPref();

        adView =  this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // All emulators
                .build();
        if (!mIsPremium) {
            adView.loadAd(adRequest);
        }

        proButton =  findViewById(R.id.proButton);
        if (mIsPremium) {
            proButton.setVisibility(View.INVISIBLE);
        }

        ImageButton helpButton =  findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpActivity.class);
                startActivity(intent);
            }
        });

        proButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyClicked();
            }
        });

        // Create and initialize BillingManager which talks to BillingLibrary
        mBillingManager = new BillingManager(this, mUpdateListener);

        homeView = findViewById(R.id.homeview);

        if (isFirstRun) {
            alertDialog();
            isFirstRun = false;
            savePref();
        }
    }

    @Override
    public void onBackPressed() {
        if (!homeView.isAniOnGoing()) {
            if (homeView.getPage() == 2) {
                homeView.backToPage(1);
            } else if (homeView.getPage() == 1) {
                homeView.backToPage(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlayed) {
            homeView.resetView();
        }
        readPref();
        homeView.setResume(isCached);
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
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.Upgrade),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBuyClicked();
                    }
                }
        );
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.Cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }
        );
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
        //Log.d("TAG", "Showing alert dialog: " + message);
        bld.create().show();
    }

    private void readPref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);// get the parameters from the Shared
        isFirstRun = prefs.getString(Constants.ISFIRSTRUN_PREF, "true").equals("true");
        isCached = prefs.getString(Constants.ISCACHED_PREF, "false").equals("true");
        mIsPremium = prefs.getString(Constants.PREMIUM, "false").equals("true");
    }

    private void savePref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);
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
                mIsPremium=false;
            }
            for (com.android.billingclient.api.Purchase purchase : purchases) {
                if (purchase.getSku().equals(BillingConstants.SKU_ADFREE)) {
                    alert(getString(R.string.pro_title), getString(R.string.pro_version));
                    proButton.setVisibility(View.INVISIBLE); // hide buy button
                    adView.setVisibility(View.INVISIBLE); // hide AD
                    mIsPremium = true;
                    savePref();
                }
            }
        }
    }
}