package com.domain.apps.snapadeal.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.domain.apps.snapadeal.AppController;
import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.appconfig.AppConfig;
import com.domain.apps.snapadeal.appconfig.Constances;
import com.domain.apps.snapadeal.classes.Category;
import com.domain.apps.snapadeal.controllers.sessions.SessionsController;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    public final static String TAG = "mainfragment";
    private static ViewPager pager;
    private ViewPagerAdapter adapter;
    private SmartTabLayout tabs;
    private FragmentActivity myContext;


    public static ViewPager getPager() {
        return pager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(myContext.getSupportFragmentManager(),
                AppConfig.TabsConfig, AppConfig.TabsConfig.size());

        // Assigning ViewPager View and setting the adapter
        pager = rootView.findViewById(R.id.pager);
        pager.setAdapter(adapter);

        pager.setOffscreenPageLimit((Constances.initConfig.Numboftabs));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PageViewEvent mPageViewEvent = new PageViewEvent();
                mPageViewEvent.position = position;
                //mPageViewEvent.title = adapter.getPageTitle(position).toString();

                switch (position) {

                    case 1:
                        mPageViewEvent.title = getString(R.string.tab_stores);
                        break;
                    case 2:
                        mPageViewEvent.title = getString(R.string.tab_events);
                        break;
                    case 3:
                        mPageViewEvent.title = getString(R.string.tab_inbox);
                        break;
                    default:
                        mPageViewEvent.title = getString(R.string.tab_offers);
                        break;

                }


                EventBus.getDefault().post(mPageViewEvent);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // Assiging the Sliding Tab Layout View
        tabs = rootView.findViewById(R.id.tabs);
        tabs.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.custom_tab_icon1, container,
                        false);

                int drawableId = Constances.initConfig.ListCats.get(position).getIcon();
                icon.setImageDrawable(getResources().getDrawable(drawableId));

                return icon;
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        /*
         *   SUPPORT RTL
         */
        if (AppController.isRTL()) {
            pager.setCurrentItem(AppConfig.TabsConfig.size() - 1);
        }

        try {
            boolean chat = getActivity().getIntent().getExtras().getBoolean("chat", false);
            if (chat) {

                for (int i = 0; i < Constances.initConfig.Numboftabs; i++) {
                    if (AppConfig.TabsConfig.get(i).getNumCat() == -1) {
                        pager.setCurrentItem(i);
                    }
                }
            }

        } catch (Exception e) {

        }


        if (Constances.initConfig.Numboftabs == 1) {
            tabs.setVisibility(View.GONE);
        }

        tabs.setDistributeEvenly(false);

        return rootView;
    }

    public void setCurrentFragment(int position) {
        pager.setCurrentItem(position);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext = (FragmentActivity) activity;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateBadges();
    }


    private void updateBadges() {
        // MainActivity.updateMessengerBadge(this.get);
        // MainActivity.updateNotificationBadge(getActivity());
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Category> tabsItems; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

        List<Integer> nbrNotifis;

        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm, List<Category> tabs, int mNumbOfTabsumb) {
            super(fm);

            this.tabsItems = tabs;
            this.NumbOfTabs = mNumbOfTabsumb;
            nbrNotifis = new ArrayList<>();


            for (int i = 0; i < NumbOfTabs; i++) {
                nbrNotifis.add(0);
            }
        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public androidx.fragment.app.Fragment getItem(int position) {

            androidx.fragment.app.Fragment fragment = null;

            /*((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                            Color.parseColor(Constances.initConfig.Colors[position]))
            );*/


            ListStoresFragment frag0 = null;


            if (AppConfig.APP_DEBUG)
                Log.e("tab", String.valueOf(AppConfig.TabsConfig.get(position).getType()));

            if (AppConfig.TabsConfig.get(position).getType() == Constances.initConfig.Tabs.EVENTS) {
                fragment = new ListEventFragment();

            } else if (AppConfig.TabsConfig.get(position).getType() == Constances.initConfig.Tabs.CHAT) {
                if (SessionsController.isLogged()) {
                    fragment = new InboxFragment();
                } else
                    fragment = new LoginFragment();

            } else if (AppConfig.TabsConfig.get(position).getType() == Constances.initConfig.Tabs.NEARBY_OFFERS) {

                fragment = new ListOffersFragment();
                Bundle b = new Bundle();
                fragment.setArguments(b);

            } else {

                fragment = new ListStoresFragment();
                Bundle b = new Bundle();
                b.putInt("category", AppConfig.TabsConfig.get(position).getNumCat());
                fragment.setArguments(b);
            }


            return fragment;

            //return fragment;
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsItems.get(position).getNameCat();
        }


        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }


    }

    public class PageViewEvent {
        public String title;
        public int position;
    }

}
