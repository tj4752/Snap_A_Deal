package com.domain.apps.snapadeal.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.fragments.ListUsersFragment;


/**
 * Created by Droideve on 7/13/2017.
 */

public class ListUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);


        setupToolbar();

        ListUsersFragment frag = new ListUsersFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.animator.fade_in_listoffres, R.animator.fade_out_listoffres);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.container, frag, ListUsersActivity.class.getName());
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    public void setupToolbar() {

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setSubtitle("E-shop");
        getSupportActionBar().setTitle("");
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView APP_TITLE_VIEW = toolbar.findViewById(R.id.toolbar_title);
        TextView APP_DESC_VIEW = toolbar.findViewById(R.id.toolbar_description);

        APP_DESC_VIEW.setVisibility(View.GONE);
        APP_TITLE_VIEW.setText(getString(R.string.people_around_me));
        APP_DESC_VIEW.setVisibility(View.GONE);
    }

}
