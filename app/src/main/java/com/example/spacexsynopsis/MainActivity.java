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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, LaunchDetailFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    private MainOverviewFragment mainOverviewFragment;
    private LaunchDetailFragment launchDetailFragment;
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private String mLaunchType;

    public static final String FILE_NAME = "pref_string.txt";




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

        setTitle(loadPreferences());

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(loadPreferences());
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
        LaunchDetailFragment fragment = LaunchDetailFragment.newInstance(launch);
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


    @Override
    public void onItemLongSelected(Launch launch) {
//        String escapedQuery = URLEncoder.encode(query, "UTF-8");
        Uri uri = Uri.parse("http://www.google.com/#q=" + launch.name);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

 

    public void retrieveLaunches(String launch){

        String launchTime =  "";


//        String launchTime = "";

//        switch (launch){
//            case "PAST":
//                launchTime = "past";
//                break;
//            default:
//                launchTime = "upcoming";
//
//        }


        String url = "https://api.spacexdata.com/v3/launches/" + launchTime;

        mainOverviewFragment.clearLaunchList();

        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Read Reverse order for Past Items
                        for(int i = 0; i< response.length(); i++) {
                            Launch launch = parseLaunch(response, i);

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


    private Launch parseLaunch(JSONArray response, int i) {

        Launch launch = new Launch();
        JSONObject launchObject;
        try {
            launchObject = response.getJSONObject(i);
            launch.setName(launchObject.getString("mission_name"));
            launch.setDate(launchObject.getString("launch_date_utc")
                    .substring(0, 19)
                    .replace("T", " ")
                    + " UTC");

            launch.setLaunchSiteName(launchObject.getJSONObject("launch_site").getString("site_name_long"));
            launch.setLaunchDetails(launchObject.getString("details"));

            JSONObject rocket = launchObject.getJSONObject("rocket");
            launch.setRocketName(rocket.getString("rocket_name"));


            JSONArray payloads = rocket.getJSONObject("second_stage").getJSONArray("payloads");
            for (int p = 0; p < payloads.length(); p++) {
                JSONObject payloadObject = payloads.getJSONObject(p);
                Payload payload = new Payload();

                payload.setManufacturer(payloadObject.getString("manufacturer"));
                payload.setNationality(payloadObject.getString("nationality"));
                payload.setPayloadId(payloadObject.getString("payload_id"));
                payload.setPayloadMass(payloadObject.getString("payload_mass_kg"));
                payload.setPayloadType(payloadObject.getString("payload_type"));

                JSONArray customersArray = payloadObject.getJSONArray("customers");
                ArrayList<String> customers = new ArrayList<String>();

                for(int c = 0; c < customersArray.length(); c++){
                    customers.add(customersArray.getString(c));
                }

                payload.setCustomers(customers);

            }

            downloadImage(launch, launchObject.getJSONObject("links").getString("mission_patch_small"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return launch;
        }

    public String loadPreferences() {
        FileInputStream fis = null;
        String prefString = "Set your own name!";
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            prefString = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return prefString;
    }


}
