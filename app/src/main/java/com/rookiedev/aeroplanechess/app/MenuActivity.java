package com.rookiedev.aeroplanechess.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rookiedev.aeroplanechess.app.constants.Constants;
import com.rookiedev.aeroplanechess.app.util.IabHelper;
import com.rookiedev.aeroplanechess.app.util.IabResult;
import com.rookiedev.aeroplanechess.app.util.Inventory;
import com.rookiedev.aeroplanechess.app.util.Purchase;
import com.rookiedev.aeroplanechess.app.view.HomeView;

public class MenuActivity extends ActionBarActivity {
    // in app billing
    private final static String SKU_PREMIUM = "premium";
    // (arbitrary) request code for the purchase flow
    private final static int RC_REQUEST = 10001;
    // if user start a new game
    public static boolean isPlayed = false;
    // is the first to play the game
    private boolean isFirstRun;
    // Does the user have the premium upgrade?
    private boolean mIsPremium = false;
    // check if the IAB setup is successful
    private boolean isIABsetupSuccess;
    // The helper object
    private IabHelper mHelper;
    /**
     * Listener that's called when we finish querying the items and subscriptions we own
     */
    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            //Log.d(TAG, "Query inventory finished.");
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) {
                return;
            }
            // Is it a failure?
            if (result.isFailure()) {
                //complain("Failed to query inventory: " + result);
                return;
            }
            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */
            //Log.d(TAG, "Query inventory was successful.");
            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            savePref();
            if (mIsPremium) {
                adView.setVisibility(View.INVISIBLE);
                proButton.setVisibility(View.INVISIBLE);
            } else if (isFirstRun) {
                alertDialog();
                isFirstRun = false;
                savePref();
            }
            //Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
        }
    };

    /**
     * Callback for when a purchase is finished
     */
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            //Log.d("TAG", "Purchase finished: " + result + ", purchase: " + purchase);
            // if we were disposed of in the meantime, quit.
            if (mHelper == null) {
                return;
            }
            if (result.isFailure()) {
                //complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                //complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            //Log.d(TAG, "Purchase successful.");
            if (purchase.getSku().equals(SKU_PREMIUM)) {
                //Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert(getString(R.string.pro_title), getString(R.string.pro_version));
                mIsPremium = true;
                proButton.setVisibility(View.INVISIBLE); // hide buy button
                adView.setVisibility(View.INVISIBLE); // hide AD
                mIsPremium = true;
                savePref();
            }
        }
    };

    // if there was a game cached already
    private boolean isCached = false;
    // views
    private HomeView homeView;
    private ImageButton proButton;
    private AdView adView;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        readPref();

        /** Look up the AdView as a resource and load a request. */
        adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR) // All emulators
                .addTestDevice("015d172c791c0215") // my test device
                .addTestDevice("04afa117002e7ebc") // my test device
                .build();
        if (!mIsPremium) {
            adView.loadAd(adRequest);
        }

        proButton = (ImageButton) findViewById(R.id.proButton);
        if (mIsPremium) {
            proButton.setVisibility(View.INVISIBLE);
        }

        ImageButton helpButton = (ImageButton) findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, HelpActivity.class);
                startActivity(intent);
            }
        });

        /** in app billing */
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxn6o3fwh9JaOp67/aINiO12/Nl6H5gnVtdjfd1w+960IX3nGgqyK1Gr5ks96u93VZFRYGoczJXLbmKnMAdPXbAb97pelwDzH3t/Y6grA2slomAeZXgPgsndOnNrWfJbOtbMuOGiXzMiEAeFqjP4Yqlh6qyF0WW6j2FvMg+1r3VkNBq6BEor26X9248yPDqA8Ov67HB26G91tQh4WMJ+7HeWJuMgYyPa8aMqAYAveNnYUU3a3QXR3M1WcsxymQHZMtLl72mjh4mzZylKTMFUgtZwpozVy6gwkqgX63JPgvbQyfy4vbJEjAUyyQQ5luH6IGrM+tFyKSl1Fo9GNdi9+MwIDAQAB";

        // Create the helper, passing it our context and the public key to verify signatures with
        //Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        //Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    //complain("Problem setting up in-app billing: " + result);
                    isIABsetupSuccess = false;
                    return;
                }
                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) {
                    isIABsetupSuccess = false;
                    return;
                }
                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                //Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
                isIABsetupSuccess = true;
            }
        });

        proButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isIABsetupSuccess) {
                    onBuyClicked();
                } else {
                    complain(getString(R.string.play_service_error));
                }
            }
        });

        homeView = (HomeView) findViewById(R.id.homeview);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) {
            return;
        }

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            // Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * We're being destroyed. It's important to dispose of the helper here!
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // very important:
        //Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
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
        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
        mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
                mPurchaseFinishedListener, payload);
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */
        return true;
    }

    private void complain(String message) {
        //Log.e("TAG", "**** TrivialDrive Error: " + message);
        alert(getString(R.string.error_title), message);
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
                ActionBarActivity.MODE_PRIVATE);// get the parameters from the Shared
        isFirstRun = prefs.getString(Constants.ISFIRSTRUN_PREF, "true").equals("true");
        isCached = prefs.getString(Constants.ISCACHED_PREF, "false").equals("true");
        mIsPremium = prefs.getString(Constants.PREMIUM, "false").equals("true");
    }

    private void savePref() {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREFS_NAME,
                ActionBarActivity.MODE_PRIVATE);
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
}
