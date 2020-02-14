package com.example.spacexsynopsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener, BlankFragment.OnFragmentInteractionListener {
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
        BlankFragment fragment = BlankFragment.newInstance(launch.name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        ft.addToBackStack(null);
        ft.replace(R.id.mainOverviewFragment, fragment, "blank_fragment").commit();
//        this.curLaunch = launch;
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        LaunchDetailFragment launchDetailFragment = new LaunchDetailFragment();
//        ft.replace(R.id.mainOverviewFragment,launchDetailFragment);
//        ft.addToBackStack("add");
//        ft.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public Launch getCurLaunch(){
        return curLaunch;
    }
}
