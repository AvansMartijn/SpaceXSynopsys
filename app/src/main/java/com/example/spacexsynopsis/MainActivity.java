package com.example.spacexsynopsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, LaunchDetailFragment.OnFragmentInteractionListener {
    private MainOverviewFragment mainOverviewFragment;
    private LaunchDetailFragment launchDetailFragment;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        retrieveLaunches("UPCOMING");

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
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        ft.addToBackStack(null);
        if(findViewById(R.id.activtiy_main_portrait) != null){
            ft.replace(R.id.mainOverviewFragment, fragment, "launch_detail_fragment").commit();
        }else {
            ft.replace(R.id.launchDetailFragment, fragment, "launch_detail_fragment").commit();
        }
    }

    private void retrieveLaunches(String launch){

//        String launchTime = "";

//        switch (launch){
//            case "PAST":
//                launchTime = "past";
//                break;
//            default:
//                launchTime = "upcoming";
//
//        }

        String url = "https://api.spacexdata.com/v3/launches/past";

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

}
