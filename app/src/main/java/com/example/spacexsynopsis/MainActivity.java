package com.example.spacexsynopsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, LaunchDetailFragment.OnFragmentInteractionListener {
    private Launch curLaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainOverviewFragment, new MainOverviewFragment());
        ft.addToBackStack("add");
        ft.commit();
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

    public Launch getCurLaunch(){
        return curLaunch;
    }
}
