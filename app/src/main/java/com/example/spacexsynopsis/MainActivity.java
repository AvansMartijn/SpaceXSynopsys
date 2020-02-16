package com.example.spacexsynopsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, LaunchDetailFragment.OnFragmentInteractionListener {
    private MainOverviewFragment mainOverviewFragment;
//    private ArrayList<Launch> upcomingLaunches;
//    private ArrayList<Launch> pastLaunches;
//    private ArrayList<Launch> favoriteLaunches;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mainOverviewFragment = new MainOverviewFragment();
        ft.replace(R.id.mainOverviewFragment, mainOverviewFragment, "main_overview_fragment");

        ft.addToBackStack("add");
        ft.commit();

        //TODO: fix loader
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = "https://api.spacexdata.com/v3/launches/past";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Read Reverse order for Past Items
                        for(int i = response.length()-1; i>=0; i--) {
                            Launch launch = new Launch("Leeg", "Datumleeg");
                            try {
                                JSONObject launchObject = response.getJSONObject(i);
                                launch = new Launch(
                                        launchObject.getString("mission_name"),
                                            launchObject.getString("launch_date_utc")
                                            .substring(0, 19)
                                            .replace("T", " ")
                                            + " UTC");
                                downloadImage(launch, launchObject.getJSONObject("links").getString("mission_patch_small"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mainOverviewFragment.addToList(launch);
                        }
                        mainOverviewFragment.getLaunchAdapter().notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

        progressDialog.dismiss();

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
        LaunchDetailFragment fragment = LaunchDetailFragment.newInstance(launch.name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.replace(R.id.mainOverviewFragment, fragment, "blank_fragment").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
