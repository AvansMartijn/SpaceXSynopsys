package com.example.spacexsynopsis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, LaunchDetailFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private MainOverviewFragment mainOverviewFragment;
    private LaunchDetailFragment launchDetailFragment;
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private String mLaunchType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.activity_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();



        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        mainOverviewFragment = new MainOverviewFragment();

        //orientation portrait
        if(findViewById(R.id.activtiy_main_portrait) != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainOverviewFragment, mainOverviewFragment, "main_overview_fragment");
            ft.commit();
        }

        if(findViewById(R.id.activity_main_landscape) != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            launchDetailFragment = new LaunchDetailFragment();
            ft.replace(R.id.mainOverviewFragment, mainOverviewFragment, "main_overview_fragment");
            ft.replace(R.id.launchDetailFragment, launchDetailFragment, "launch_detail_fragment");
            ft.commit();
        }

        navigationView.setCheckedItem(R.id.nav_upcoming);
        if(savedInstanceState != null){
            mLaunchType = savedInstanceState.getString("launchType");
        }else{
            mLaunchType = "UPCOMING";
        }

        retrieveLaunches(mLaunchType);



    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_upcoming:
                mLaunchType = "UPCOMING";
                retrieveLaunches(mLaunchType);
                break;
            case R.id.nav_past:
                mLaunchType = "PAST";
                retrieveLaunches(mLaunchType);
                break;
            case R.id.nav_fav:

                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void downloadImage(final Launch launch, String url)
    {
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        launch.setMissionPatch(bitmap);
                        mainOverviewFragment.getLaunchAdapter().notifyDataSetChanged();
                    }
                }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(imageRequest);
    }


    @Override
    public void onItemSelected(Launch launch) {
        LaunchDetailFragment fragment = LaunchDetailFragment.newInstance(launch.name, launch.date);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        ft.addToBackStack(null);
        if(findViewById(R.id.activtiy_main_portrait) != null){
            ft.replace(R.id.mainOverviewFragment, fragment, "launch_detail_fragment").commit();
        }else {
            ft.replace(R.id.launchDetailFragment, fragment, "launch_detail_fragment").commit();
        }
    }

    public void retrieveLaunches(String launch){

        String launchTime =  "";

        switch (launch){
            case "PAST":
                launchTime = "past";
                break;
            default:
                launchTime = "upcoming";

        }

        String url = "https://api.spacexdata.com/v3/launches/" + launchTime;
        mainOverviewFragment.clearLaunchList();

        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Read Reverse order for Past Items
                        for(int i = 0; i< response.length(); i++) {
                            Launch launch = new Launch("Leeg", "Datumleeg");
                            try {
                                JSONObject launchObject = response.getJSONObject(i);
                                String missionName = launchObject.getString("mission_name");
                                String launchDateUTC = launchObject.getString("launch_date_utc")
                                        .substring(0, 19)
                                        .replace("T", " ")
                                        + " UTC";

                                launch = new Launch(missionName, launchDateUTC);
                                downloadImage(launch, launchObject.getJSONObject("links").getString("mission_patch_small"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mainOverviewFragment.addToLaunchList(launch);
                        }
                        mainOverviewFragment.getLaunchAdapter().notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLaunchType = savedInstanceState.getString("launchType");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("launchType", mLaunchType);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
