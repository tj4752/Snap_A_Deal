package com.domain.apps.snapadeal.activities;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.domain.apps.snapadeal.AppController;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.Services.BusMessage;
import com.domain.apps.snapadeal.Services.NotifyDataNotificationEvent;
import com.domain.apps.snapadeal.appconfig.AppConfig;
import com.domain.apps.snapadeal.appconfig.Constances;
import com.domain.apps.snapadeal.classes.Notification;
import com.domain.apps.snapadeal.controllers.SettingsController;
import com.domain.apps.snapadeal.controllers.categories.CategoryController;
import com.domain.apps.snapadeal.controllers.users.UserController;
import com.domain.apps.snapadeal.dtmessenger.MessengerHelper;
import com.domain.apps.snapadeal.fragments.MainFragment;
import com.domain.apps.snapadeal.helper.LocalHelper;
import com.domain.apps.snapadeal.load_manager.ViewManager;
import com.domain.apps.snapadeal.navigationdrawer.NavigationDrawerFragment;
import com.domain.apps.snapadeal.network.VolleySingleton;
import com.domain.apps.snapadeal.network.api_request.SimpleRequest;
import com.domain.apps.snapadeal.parser.api_parser.CategoryParser;
import com.domain.apps.snapadeal.parser.tags.Tags;
import com.domain.apps.snapadeal.utils.CommunApiCalls;
import com.domain.apps.snapadeal.utils.Session;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
import static com.domain.apps.snapadeal.appconfig.AppConfig.APP_DEBUG;
import static com.domain.apps.snapadeal.appconfig.AppConfig.SHOW_ADS;
import static com.domain.apps.snapadeal.appconfig.AppConfig.SHOW_ADS_IN_HOME;
import static com.domain.apps.snapadeal.appconfig.AppConfig.SHOW_INTERSTITIAL_ADS_IN_STARTUP;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_description)
    TextView toolbarDescription;
    @BindView(R.id.main_container)
    LinearLayout mainContainer;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.ads)
    LinearLayout ads;


    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final int REQUEST_CHECK_SETTINGS_MAIN = 0x2;
    public static int height = 0;
    public static int width = 0;
    public static Menu mainMenu;
    private static boolean opened = false;
    public ViewManager mViewManager;
    private InterstitialAd mInterstitialAd;
    private Session session;

    /**********************************************/

    //init request http
    private RequestQueue queue;

    //SHARED ¨PREFERENCES
    private Tracker mTracker;

    /************   EVENT ALERT *******************/


    public static boolean isOpend() {
        return opened;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static void updateMessengerBadge(FragmentActivity activity) {

        try {

            if (MessengerHelper.NbrMessagesManager.getNbrTotalMessages() > 0) {
                ActionItemBadge.update(activity, mainMenu.findItem(R.id.messenger_action), CommunityMaterial.Icon.cmd_comment_multiple_outline,
                        ActionItemBadge.BadgeStyles.RED,
                        MessengerHelper.NbrMessagesManager.getNbrTotalMessages());
            } else {
                ActionItemBadge.hide(mainMenu.findItem(R.id.messenger_action));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void updateNotificationBadge(FragmentActivity activity) {

        try {

            if (Notification.notificationsUnseen > 0) {
                ActionItemBadge.update(activity, mainMenu.findItem(R.id.notification_action), CommunityMaterial.Icon.cmd_bell_outline,
                        ActionItemBadge.BadgeStyles.RED,Notification.notificationsUnseen);
            } else {
                ActionItemBadge.hide(mainMenu.findItem(R.id.notification_action));
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isAppInForeground(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

            return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
        } else {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                return true;
            }

            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            // App is foreground, but screen is locked, so show notification
            return km.inKeyguardRestrictedInputMode();
        }
    }

    @Override
    protected void onDestroy() {

        if (adView != null)
            adView.destroy();
        super.onDestroy();
        opened = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();


        //initialize the Google Mobile Ads SDK at app launch
        MobileAds.initialize(getApplicationContext(), getString(R.string.ad_app_id));

        Display display = getWindowManager().getDefaultDisplay();
        width = getScreenWidth();
        height = display.getHeight();

        //Initialize web service API
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        //Show Interstitial Ads

        //Show Banner Ads
        if (SHOW_ADS && SHOW_ADS_IN_HOME) {

            if (APP_DEBUG)
                Toast.makeText(this, "SHOW_ADS_IN_HOME:" + getResources()
                        .getString(R.string.banner_ad_unit_id), Toast.LENGTH_LONG).show();

            adView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice("4A55E4EA2535643C0D74A5A73C4F550A")
                    .addTestDevice("3CB74DFA141BF4D0823B8EA7D94531B5")
                    .build();


            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    adView.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    adView.setVisibility(View.VISIBLE);
                }
            });
        }

        mViewManager = new ViewManager(this);
        mViewManager.setLoadingLayout(findViewById(R.id.loading));
        mViewManager.setResultLayout(findViewById(R.id.content_my_store));
        mViewManager.setErrorLayout(findViewById(R.id.error));
        mViewManager.setEmpty(findViewById(R.id.empty));
        mViewManager.showResult();

        setupToolbar();


        int size = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

        switch (size) {

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:


                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                NavigationDrawerFragment fragNDF = new NavigationDrawerFragment();

                FragmentTransaction transactionNDF = getFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.animator.fade_in_listoffres, R.animator.fade_out_listoffres);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transactionNDF.replace(R.id.nav_container, fragNDF, MainFragment.TAG);
                //transaction.addToBackStack(null);

                // Commit the transaction
                transactionNDF.commit();

                break;


            case Configuration.SCREENLAYOUT_SIZE_LARGE:


                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                NavigationDrawerFragment fragNDFL = new NavigationDrawerFragment();

                FragmentTransaction transactionNDFL = getFragmentManager().beginTransaction();
                //transaction.setCustomAnimations(R.animator.fade_in_listoffres, R.animator.fade_out_listoffres);

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transactionNDFL.replace(R.id.nav_container, fragNDFL, MainFragment.TAG);
                //transaction.addToBackStack(null);

                // Commit the transaction
                transactionNDFL.commit();


                break;

            default:

                NavigationDrawerFragment NaDrawerFrag =
                        (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.frag_nav_drawer);

                NaDrawerFrag.setUp(
                        R.id.frag_nav_drawer,
                        findViewById(R.id.drawerLayout),
                        toolbar);
                break;
        }


        MainFragment frag = new MainFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.animator.fade_in_listoffres, R.animator.fade_out_listoffres);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.main_container, frag, MainFragment.TAG);
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        loadCategoriesApiCall();


        //Show Interstitial Ads
        if (SHOW_ADS && SHOW_INTERSTITIAL_ADS_IN_STARTUP) {
            //show ad
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestNewInterstitial();
                }
            }, 5000);
        }
        UserController.checkUserConnection(this);

        /*(new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                getUpcomingEvent();
            }
        }, 6000); */


        //get notification count
        CommunApiCalls.countUnseenNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {

        if (adView != null)
            adView.pause();
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        mainMenu = menu;
        updateMessengerBadge();
        updateNotificationBadge(Notification.notificationsUnseen);

        return true;
    }

    private void updateMessengerBadge() {

        if (MessengerHelper.NbrMessagesManager.getNbrTotalMessages() > 0) {
            ActionItemBadge.update(this, mainMenu.findItem(R.id.messenger_action), CommunityMaterial.Icon.cmd_comment_multiple_outline,
                    ActionItemBadge.BadgeStyles.RED,
                    MessengerHelper.NbrMessagesManager.getNbrTotalMessages());
        } else {
            ActionItemBadge.hide(mainMenu.findItem(R.id.messenger_action));
        }
    }

    private void updateNotificationBadge(int notification) {

        if (notification > 0) {
            ActionItemBadge.update(this, mainMenu.findItem(R.id.notification_action), CommunityMaterial.Icon.cmd_bell_outline,
                    ActionItemBadge.BadgeStyles.RED,notification);
        } else {
            ActionItemBadge.hide(mainMenu.findItem(R.id.notification_action));
        }
    }

    //Manage menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String lang = null;

        if (R.id.notification_action == item.getItemId()) {

            //start notification activity
            Intent intent = new Intent(this,NotificationActivity.class);
            startActivity(intent);

        }else if (R.id.messenger_action == item.getItemId()) {
            MainFragment.getPager().setCurrentItem(3);
        } else if (R.id.en_ln == item.getItemId()) {
            //LocalHelper.setLocale(this,"en");
            lang = "en";
        } else if (R.id.fr_ln == item.getItemId()) {
            lang = "fr";
        } else if (R.id.es_ln == item.getItemId()) {
            lang = "es";
        } else if (R.id.ar_ln == item.getItemId()) {
            lang = "ar";
        }

        if (lang != null) {
            //Change Application level locale
            LocalHelper.setLocale(getBaseContext(), lang);

            //It is required to recreate the activity to reflect the change in UI.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
            } else {
                ActivityCompat.finishAffinity(this);
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        // privacy & Condition of uses :
        // Checking for first time launch - before calling s
        session = new Session(getApplicationContext());
        if (session.isFirstTimeLaunch()) {
            showPrivacyUsesConditionsDailog();
        }


        opened = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);

    }

    @Subscribe
    public void onNewNotifs(BusMessage bus) {

        if (bus.getType() == BusMessage.GET_NBR_NEW_NOTIFS) {
            if (APP_DEBUG)
                if (MessengerHelper.NbrMessagesManager.getNbrTotalMessages() > 0) {
                    Toast.makeText(this, "New message " + MessengerHelper.NbrMessagesManager.getNbrTotalMessages()
                            , Toast.LENGTH_LONG).show();
                }
            updateMessengerBadge();
        }

    }


    // This method will be called when a Notification is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyDataNotificationEvent event) {
        if(event.message.equals("update_badges")){
            updateNotificationBadge(Notification.notificationsUnseen);
        }
    }

    @Subscribe
    public void onPageChangedMainFragement(MainFragment.PageViewEvent event) {
        toolbarTitle.setText(event.title);
    }

    public void setupToolbar() {

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarDescription.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adView != null)
            adView.resume();

        mTracker.setScreenName("Image~" + MainActivity.class.getName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        try {
            updateMessengerBadge();
        } catch (Exception e) {
        }
    }

    //Get all categories from server and save them in  the database
    private void loadCategoriesApiCall() {


        SimpleRequest request = new SimpleRequest(Request.Method.GET,
                Constances.API.API_USER_GET_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (APP_DEBUG) {
                        Log.e("catsResponse", response);
                    }

                    JSONObject jsonObject = new JSONObject(response);
                    // Log.e("response", jsonObject.toString());
                    final CategoryParser mCategoryParser = new CategoryParser(jsonObject);
                    int success = Integer.parseInt(mCategoryParser.getStringAttr(Tags.SUCCESS));
                    if (success == 1) {
                        //database.deleteCats();
                        //update list categories
                        CategoryController.removeAll();
                        CategoryController.insertCategories(
                                mCategoryParser.getCategories()
                        );

                    }

                } catch (JSONException e) {
                    //send a rapport to support
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (APP_DEBUG) {
                    Log.e("ERROR", error.toString());
                }

            }


        }) {


        };

        request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        String event;
        if (extras != null) {
            event = extras.getString("Notified");
            if (APP_DEBUG) {
                Log.e("Notified", "Event notified  " + event);
            }
        } else {
            if (APP_DEBUG) {
                Log.e("Notified", "Extras are NULL");
            }

        }
    }

    private void requestNewInterstitial() {

        if (APP_DEBUG)
            Toast.makeText(this, "requestNewInterstitial:" + getResources()
                    .getString(R.string.ad_interstitial_id), Toast.LENGTH_LONG).show();
        // Load Interstitial Ad
        // Initializing InterstitialAd - ADMob
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial_id));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4A55E4EA2535643C0D74A5A73C4F550A")
                .addTestDevice("3CB74DFA141BF4D0823B8EA7D94531B5")
                .build();
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Full screen advertise will show only after loading complete
                mInterstitialAd.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        NavigationDrawerFragment.getInstance().closeDrawers();

        if (AppConfig.RATE_US_FORCE) {
            if (SettingsController.rateOnApp(this)) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }


    private void showPrivacyUsesConditionsDailog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_privacy_conditions);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        String privacyMessageNonFormated = getResources().getString(R.string.msg_dialog_privacy_policy);
        ((TextView) dialog.findViewById(R.id.tv_content)).setText(Html.fromHtml(String.format(privacyMessageNonFormated, "<a href='" + getResources().getString(R.string.TERMS_OF_USE_URL) + "'>Terms-Conditions</a>", "<a href='" + getResources().getString(R.string.PRIVACY_POLICY_URL) + "'>Privacy-Policy</a>")));

        ((TextView) dialog.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());


        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //disable privacy after the first time launch s
                session.setFirstTimeLaunch(false);
                //close the dialog
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the app
                finish();
                System.exit(0);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
