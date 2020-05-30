package com.domain.apps.snapadeal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.domain.apps.snapadeal.GPS.GPStracker;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.activities.MainActivity;
import com.domain.apps.snapadeal.activities.OfferDetailActivity;
import com.domain.apps.snapadeal.adapter.lists.OfferListAdapter;
import com.domain.apps.snapadeal.appconfig.AppConfig;
import com.domain.apps.snapadeal.appconfig.Constances;
import com.domain.apps.snapadeal.classes.Offer;
import com.domain.apps.snapadeal.controllers.ErrorsController;
import com.domain.apps.snapadeal.controllers.stores.OffersController;
import com.domain.apps.snapadeal.load_manager.ViewManager;
import com.domain.apps.snapadeal.network.ServiceHandler;
import com.domain.apps.snapadeal.network.VolleySingleton;
import com.domain.apps.snapadeal.network.api_request.SimpleRequest;
import com.domain.apps.snapadeal.parser.api_parser.OfferParser;
import com.domain.apps.snapadeal.parser.tags.Tags;
import com.domain.apps.snapadeal.utils.DateUtils;
import com.domain.apps.snapadeal.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.realm.RealmList;

import static com.domain.apps.snapadeal.R.layout.fragment_offers_list;
import static com.domain.apps.snapadeal.appconfig.AppConfig.APP_DEBUG;

public class ListOffersFragment extends Fragment
        implements OfferListAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener, ViewManager.CustomView, View.OnClickListener {

    public ViewManager mViewManager;
    //loading
    public SwipeRefreshLayout swipeRefreshLayout;
    //for scrolling params
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLayoutManager;
    private int listType = 1;
    private RecyclerView list;
    private OfferListAdapter adapter;
    //init request http
    private RequestQueue queue;
    private boolean loading = true;
    //pager
    private int COUNT = 0;
    private int REQUEST_PAGE = 1;
    private GPStracker mGPS;
    private List<Offer> listStores = new ArrayList<>();
    private int REQUEST_RANGE_RADIUS = -1;
    private String REQUEST_SEARCH = "";

    private String current_date;

    private int store_id = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        REQUEST_RANGE_RADIUS = Integer.parseInt(getResources().getString(R.string.distance_max_display_route));

        current_date = DateUtils.getUTC("yyyy-MM-dd H:m:s");

    }


    private void updateBadges() {
        MainActivity.updateMessengerBadge(getActivity());
        MainActivity.updateNotificationBadge(getActivity());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateBadges();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.search_icon) {

            SearchDialog.newInstance(getContext()).setOnSearchListener(new SearchDialog.Listener() {
                @Override
                public void onSearchClicked(SearchDialog mSearchDialog, String value, int radius) {

                    if (mSearchDialog.isShowing())
                        mSearchDialog.dismiss();

                    if (AppConfig.APP_DEBUG)
                        Toast.makeText(getContext(), value + " " + radius, Toast.LENGTH_LONG).show();

                    REQUEST_RANGE_RADIUS = radius;
                    REQUEST_SEARCH = value;
                    REQUEST_PAGE = 1;
                    getOffers(1);

                }
            }).setHeader(getString(R.string.searchOnOffers)).showDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(fragment_offers_list, container, false);


        try {
            store_id = getArguments().getInt("store_id");
        } catch (Exception e) {
            store_id = 0;
        }

        try {
            //load from assets
           /*if(!ServiceHandler.isNetworkAvailable(getActivity())){

               if(Constances.ENABLE_OFFLINE)
                listStores = loader.parseStoresFromAssets(getActivity(),mCat.getNumCat());
           }*/

        } catch (Exception e) {

            e.printStackTrace();
        }

        mGPS = new GPStracker(getActivity());
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();

        mViewManager = new ViewManager(getActivity());
        mViewManager.setLoadingLayout(rootView.findViewById(R.id.loading));
        mViewManager.setResultLayout(rootView.findViewById(R.id.content_my_store));
        mViewManager.setErrorLayout(rootView.findViewById(R.id.error));
        mViewManager.setEmpty(rootView.findViewById(R.id.empty));
        mViewManager.setCustumizeView(this);


        list = rootView.findViewById(R.id.list);

        adapter = new OfferListAdapter(getActivity(), listStores);
        adapter.setClickListener(this);


        list.setHasFixedSize(true);
        final RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        list.setLayoutManager(mLayoutManager);

        list.setItemAnimator(new DefaultItemAnimator());
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(adapter);

//
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = ((GridLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
                if (loading) {

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        if (ServiceHandler.isNetworkAvailable(getContext())) {
                            if (COUNT > adapter.getItemCount())
                                getOffers(REQUEST_PAGE);
                        } else {
                            Toast.makeText(getContext(), "Network not available ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        swipeRefreshLayout = rootView.findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(this);


        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary
        );


        if (ServiceHandler.isNetworkAvailable(this.getActivity())) {
            getOffers(REQUEST_PAGE);

        } else {

            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), getString(R.string.check_network), Toast.LENGTH_LONG).show();
            if (adapter.getItemCount() == 0)
                mViewManager.error();
        }


        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {

        Intent intent = new Intent(getActivity(), OfferDetailActivity.class);
        intent.putExtra("offer_id", adapter.getItem(position).getId());
        startActivity(intent);

    }

    public void getOffers(final int page) {

        mGPS = new GPStracker(getActivity());
        swipeRefreshLayout.setRefreshing(true);

        if (adapter.getItemCount() == 0)
            mViewManager.loading();

        SimpleRequest request = new SimpleRequest(Request.Method.POST,
                Constances.API.API_GET_OFFERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (APP_DEBUG) {
                        Log.e("responseOffersString", response);
                    }

                    JSONObject jsonObject = new JSONObject(response);


                    //Log.e("response",response);

                    final OfferParser mOfferParser = new OfferParser(jsonObject);
                    // List<Store> list = mStoreParser.getEventRealm();
                    COUNT = 0;
                    COUNT = mOfferParser.getIntArg(Tags.COUNT);
                    mViewManager.showResult();


                    //check server permission and display the errors
                    if (mOfferParser.getSuccess() == -1) {
                        ErrorsController.serverPermissionError(getActivity());
                    }

                    if (page == 1) {

                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RealmList<Offer> list = mOfferParser.getOffers();

                                adapter.removeAll();
                                for (int i = 0; i < list.size(); i++) {
                                    Offer ofr = list.get(i);
                                    if (mGPS.getLongitude() == 0 && mGPS.getLatitude() == 0) {
                                        ofr.setDistance((double) 0);
                                    }
                                    // if (list.get(i).getDistance() <= REQUEST_RANGE_RADIUS)
                                    adapter.addItem(ofr);
                                }

                                OffersController.removeAll();
                                //set it into database
                                OffersController.insertOffers(list);

                                swipeRefreshLayout.setRefreshing(false);
                                loading = true;

                                mViewManager.showResult();

                                if (COUNT > adapter.getItemCount())
                                    REQUEST_PAGE++;
                                if (COUNT == 0 || adapter.getItemCount() == 0) {
                                    mViewManager.empty();
                                }


                                if (APP_DEBUG) {
                                    Log.e("count ", COUNT + " page = " + page);
                                }

                            }
                        }, 800);
                    } else {
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                RealmList<Offer> list = mOfferParser.getOffers();

                                for (int i = 0; i < list.size(); i++) {
                                    Offer ofr = list.get(i);
                                    if (mGPS.getLongitude() == 0 && mGPS.getLatitude() == 0) {
                                        ofr.setDistance((double) 0);
                                    }
                                    // if (list.get(i).getDistance() <= REQUEST_RANGE_RADIUS)
                                    adapter.addItem(ofr);
                                }


                                //set it into database
                                OffersController.insertOffers(list);

                                swipeRefreshLayout.setRefreshing(false);
                                mViewManager.showResult();
                                loading = true;
                                if (COUNT > adapter.getItemCount())
                                    REQUEST_PAGE++;

                                if (COUNT == 0 || adapter.getItemCount() == 0) {
                                    mViewManager.empty();
                                }
                            }
                        }, 800);

                    }

                } catch (JSONException e) {
                    //send a rapport to support
                    if (APP_DEBUG)
                        e.printStackTrace();

                    if (adapter.getItemCount() == 0)
                        mViewManager.error();


                    swipeRefreshLayout.setRefreshing(false);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (APP_DEBUG) {
                    Log.e("ERROR", error.toString());
                }

                mViewManager.error();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (mGPS.canGetLocation()) {
                    params.put("latitude", mGPS.getLatitude() + "");
                    params.put("longitude", mGPS.getLongitude() + "");
                }

                params.put("token", Utils.getToken(getActivity()));
                params.put("mac_adr", ServiceHandler.getMacAddr());

                params.put("limit", "30");
                params.put("page", page + "");
                params.put("search", REQUEST_SEARCH);
                params.put("date", current_date);
                params.put("timezone", TimeZone.getDefault().getID());

                if (store_id > 0)
                    params.put("store_id", String.valueOf(store_id));

                if (APP_DEBUG) {
                    Log.e("ListStoreFragment", "  params getOffers :" + params.toString());
                }


                if (REQUEST_RANGE_RADIUS != -1) {
                    if (REQUEST_RANGE_RADIUS <= 99)
                        params.put("radius", String.valueOf((REQUEST_RANGE_RADIUS * 1024)));
                }

                return params;
            }

        };


        request.setRetryPolicy(new DefaultRetryPolicy(SimpleRequest.TIME_OUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);

    }

    @Override
    public void onRefresh() {

        mGPS = new GPStracker(getActivity());
        if (mGPS.canGetLocation()) {

            REQUEST_SEARCH = "";
            REQUEST_PAGE = 1;
            REQUEST_RANGE_RADIUS = -1;
            getOffers(1);

        } else {
            swipeRefreshLayout.setRefreshing(false);
            mGPS.showSettingsAlert();

            if (adapter.getItemCount() == 0)
                mViewManager.error();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            //Toast.makeText(getActivity(), "  is Liked  :"+args.get("isLiked"), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void customErrorView(View v) {

        Button retry = v.findViewById(R.id.btn);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mGPS = new GPStracker(getActivity());

                if (!mGPS.canGetLocation() && listType == 1)
                    mGPS.showSettingsAlert();

                getOffers(1);
                REQUEST_PAGE = 1;
                mViewManager.loading();
            }
        });

    }

    @Override
    public void customLoadingView(View v) {


    }

    @Override
    public void customEmptyView(View v) {

        Button btn = v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewManager.loading();
                getOffers(1);
                REQUEST_PAGE = 1;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onClick(View view) {

    }
}
