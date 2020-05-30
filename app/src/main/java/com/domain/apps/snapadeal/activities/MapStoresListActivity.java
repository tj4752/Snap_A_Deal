package com.domain.apps.snapadeal.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.domain.apps.snapadeal.GPS.GPStracker;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.animation.Animation;
import com.domain.apps.snapadeal.appconfig.Constances;
import com.domain.apps.snapadeal.classes.Store;
import com.domain.apps.snapadeal.load_manager.ViewManager;
import com.domain.apps.snapadeal.network.VolleySingleton;
import com.domain.apps.snapadeal.network.api_request.SimpleRequest;
import com.domain.apps.snapadeal.parser.api_parser.StoreParser;
import com.domain.apps.snapadeal.parser.tags.Tags;
import com.domain.apps.snapadeal.utils.MapsUtils;
import com.domain.apps.snapadeal.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.domain.apps.snapadeal.appconfig.AppConfig.APP_DEBUG;

public class MapStoresListActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar_description)
    TextView toolbarDescription;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.ratingBar2)
    RatingBar ratingBar2;
    @BindView(R.id.rate)
    TextView rate;
    @BindView(R.id.closeLayout)
    ImageButton closeLayout;
    @BindView(R.id.store_focus_layout)
    LinearLayout store_focus_layout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.content_my_store)
    LinearLayout content_my_store;

    private GoogleMap mMap;
    private Context context;
    public ViewManager mViewManager;
    private LatLng myPosition;
    private int COUNT = 0;
    private Toolbar toolbar;
    //init request http
    private RequestQueue queue;
    private GPStracker mGPS;
    private LinearLayout content;
    private boolean requestStarted = false;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        ButterKnife.bind(this);

        context = this;
        setupToolbar();

        mViewManager = new ViewManager(this);
        mViewManager.setLoadingLayout(findViewById(R.id.loading));
        mViewManager.setResultLayout(findViewById(R.id.content_my_store));
        mViewManager.setErrorLayout(findViewById(R.id.error));
        mViewManager.setEmpty(findViewById(R.id.empty));
        mViewManager.loading();

        initStoreFocusLayout();


        mGPS = new GPStracker(this);

        if (!mGPS.canGetLocation())
            mGPS.showSettingsAlert();


        queue = VolleySingleton.getInstance(this).getRequestQueue();


        //GET TREAD GEO POINT FROM INTENT


        //INITIALIZE MY LOCATION
        mGPS = new GPStracker(this);


       /* mMap = ((SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();*/

        attachMap();

        myPosition = new LatLng(mGPS.getLatitude(), mGPS.getLongitude());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, context.getString(R.string.searching_for_stores_bearby), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mGPS = new GPStracker(context);
                if (mGPS.canGetLocation()) {
                    if (mMap != null) {
                        LatLng po = new LatLng(
                                mMap.getCameraPosition().target.latitude,
                                mMap.getCameraPosition().target.longitude
                        );
                        float zoomLevel = (float) 12.0;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(po, zoomLevel));

                        getStores(po, true);
                    }
                } else {
                    mGPS.showSettingsAlert();
                }

            }
        });


    }

    private void initStoreFocusLayout() {

        store_focus_layout.setVisibility(View.GONE);

    }

    private void hideStoreFocusLayout() {

        if (store_focus_layout.isShown())
            Animation.hideWithZoomEffect(store_focus_layout);

    }

    private void showStoreFocusLayout(final Store store) {


        TextView title = name;
        RatingBar rateBar = ratingBar2;
        TextView rateNbr = rate;

        title.setText(store.getName());
        rateBar.setRating((float) store.getVotes());

        float rated = (float) store.getVotes();
        DecimalFormat decim = new DecimalFormat("#.##");

        rateNbr.setText(decim.format(rated) + "  (" + store.getNbr_votes() + ")");


        store_focus_layout.setVisibility(View.VISIBLE);
        Animation.startCustomZoom(store_focus_layout);
        //give permission to hide this layout after 3 second


        closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideStoreFocusLayout();

            }
        });

        store_focus_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideStoreFocusLayout();

                int id = store.getId();
                Intent intent = new Intent(MapStoresListActivity.this, StoreDetailActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });


    }

    private void attachMap() {

        try {

            SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mSupportMapFragment == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                mSupportMapFragment = SupportMapFragment.newInstance();
                mSupportMapFragment.setRetainInstance(true);
                fragmentTransaction.replace(R.id.mapping, mSupportMapFragment).commit();
            }
            if (mSupportMapFragment != null) {
                mSupportMapFragment.getMapAsync(MapStoresListActivity.this);
            }

        } catch (Exception e) {

        }

    }

    private void moveHandler() {

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (requestStarted) {

                    LatLng po = new LatLng(
                            mMap.getCameraPosition().target.latitude,
                            mMap.getCameraPosition().target.longitude
                    );

                    // getStores(po,true);
                }
            }
        }, 3000);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        initMapping();

        mMap.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
            @Override
            public void onCameraMoveCanceled() {

                if (APP_DEBUG)
                    Log.e("onCameraMoveCanceled", String.valueOf(mMap.getCameraPosition().target.latitude));
            }
        });


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if (APP_DEBUG)
                    Log.e("onCameraMove", String.valueOf(mMap.getCameraPosition().target.latitude));

                moveHandler();

            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                if (APP_DEBUG)
                    Log.e("onCameraMoveStarted", String.valueOf(i));

            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (APP_DEBUG)
                    Log.e("onCameraIdle", String.valueOf(mMap.getCameraPosition().target.latitude));
            }
        });


    }


    private void initMapping() {

        if (mMap != null) {

            mMap.getUiSettings().setZoomControlsEnabled(true);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);

            mGPS = new GPStracker(getBaseContext());
            final LatLng po = new LatLng(mGPS.getLatitude(), mGPS.getLatitude());

            getStores(po, false);

        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    private void getStores(final LatLng position, final boolean refresh) {

        requestStarted = true;

        if (refresh == false) {
            mViewManager.loading();
        }

        SimpleRequest request = new SimpleRequest(Request.Method.POST,
                Constances.API.API_USER_GET_STORES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    requestStarted = false;
                    if (APP_DEBUG)
                        Log.e("____response", response);

                    JSONObject jsonObject = new JSONObject(response);
                    final StoreParser mStoreParser = new StoreParser(jsonObject);

                    COUNT = mStoreParser.getIntArg(Tags.COUNT);
                    int success = Integer.parseInt(mStoreParser.getStringAttr("success"));

                    if (success == 1) {

                        if (COUNT > 0) {

                            if (refresh == false) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14));
                                mViewManager.showResult();
                            }

                            final List<Store> list = mStoreParser.getStore();

                            Bitmap b = ((BitmapDrawable) Utils.changeDrawableIconMap(
                                    MapStoresListActivity.this, R.drawable.ic_marker)).getBitmap();
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(b);

                            if (refresh == true) {
                                mMap.clear();
                            }

                            for (int i = 0; i < list.size(); i++) {

                                String imageUrl = null;

                                if (list.get(i).getListImages() != null && list.get(i).getListImages().size() > 0) {
                                    imageUrl = list.get(i).getListImages().get(0).getUrl100_100();
                                }

                                if (imageUrl != null) {

                                    final int finalI = i;


                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(imageUrl)
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                    String promo = null;
                                                    if (!list.get(finalI).getLastOffer().equals("")) {
                                                        promo = list.get(finalI).getLastOffer();
                                                    }

                                                    Marker marker = null;
                                                    marker = mMap.addMarker(

                                                            MapsUtils.generateMarker(MapStoresListActivity.this,
                                                                    String.valueOf(list.get(finalI).getId()),
                                                                    new LatLng(list.get(finalI).getLatitude(), list.get(finalI).getLongitude()
                                                                    ),
                                                                    resource,
                                                                    promo
                                                            ).draggable(false)

                                                    );

                                                    marker.setTag(finalI);
                                                    MapsUtils.addMarker(String.valueOf(list.get(finalI).getId()), marker);

                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });

                                    /*Picasso.with(context).load(imageUrl).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Glide.LoadedFrom from) {


                                            String promo = null;
                                            if(!list.get(finalI).getLastOffer().equals("")){
                                                promo= list.get(finalI).getLastOffer();
                                            }

                                            Marker marker=null;
                                            marker = mMap.addMarker(

                                                    MapsUtils.generateMarker( MapStoresListActivity.this,
                                                            String.valueOf(list.get(finalI).getId())     ,
                                                            new LatLng(list.get(finalI).getLatitude(), list.get(finalI).getLongitude()
                                                            ),
                                                            bitmap,
                                                            promo
                                                    ).draggable(false)

                                            );

                                            marker.setTag(finalI);
                                            MapsUtils.addMarker(  String.valueOf(list.get(finalI).getId())  ,marker);

                                        }
                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }
                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    });*/


                                } else {

                                    String promo = null;
                                    if (!list.get(i).getLastOffer().equals("")) {
                                        promo = list.get(i).getLastOffer();
                                    }

                                    Marker marker = null;
                                    marker = mMap.addMarker(

                                            MapsUtils.generateMarker(MapStoresListActivity.this,
                                                    String.valueOf(list.get(i).getId()),
                                                    new LatLng(list.get(i).getLatitude(), list.get(i).getLongitude()
                                                    ),
                                                    null,
                                                    promo
                                            ).draggable(false)

                                    );

                                    marker.setTag(i);
                                    MapsUtils.addMarker(String.valueOf(list.get(i).getId()), marker);

                                }


                            }

                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {

                                    int position = (int) (marker.getTag());

                                    if (APP_DEBUG)
                                        Toast.makeText(MapStoresListActivity.this, list.get(position).getName(),
                                                Toast.LENGTH_LONG).show();

//                                    int id = list.get(position).getId();
//
//                                    Intent intent = new Intent(MapStoresListActivity.this, StoreDetailActivity.class);
//                                    intent.putExtra("id", id);
//                                    startActivity(intent);

                                    showStoreFocusLayout(list.get(position));

                                    return false;
                                }
                            });

                        } else {
                            mViewManager.empty();
                        }
                    }


                } catch (JSONException e) {
                    //send a rapport to support
                    e.printStackTrace();

                    requestStarted = false;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (APP_DEBUG) {
                    Log.e("ERROR", error.toString());
                }

                //mViewManager.error();
                requestStarted = false;
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (mGPS.canGetLocation()) {
                    params.put("latitude", String.valueOf(position.latitude));
                    params.put("longitude", String.valueOf(position.longitude));
                }


                // params.put("token", Utils.getToken(getBaseContext()));
                // params.put("mac_adr", ServiceHandler.getMacAddress(getBaseContext()));

                params.put("limit", context.getResources().getString(R.string.NBR_STORES_MAX_GEO_MAPS));
                params.put("page", "1");
                params.put("order_by", "-1");
//                params.put("type", "1");
//                params.put("status", "1");


                return params;
            }

        };


        request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);


    }


    public void setupToolbar() {

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setSubtitle("E-shop");
        getSupportActionBar().setTitle("---");
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //toolbarTitle.setVisibility(View.GONE);
        //Utils.setFont(.+);
        //Utils.setFont(.+);

        toolbarTitle.setText(getString(R.string.MapStores));
        toolbarDescription.setVisibility(View.GONE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.righttoleft_enter, R.anim.righttoleft_exit);

        } else if (id == R.id.action_refresh) {

            mGPS = new GPStracker(context);
            if (mGPS.canGetLocation()) {
                if (mMap != null) {
                    LatLng po = new LatLng(
                            mMap.getCameraPosition().target.latitude,
                            mMap.getCameraPosition().target.longitude
                    );
                    getStores(po, true);
                }
            } else {
                mGPS.showSettingsAlert();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


}
