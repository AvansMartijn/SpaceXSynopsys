package com.example.spacexsynopsis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainOverviewFragment.OnItemSelectListener {
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
        this.curLaunch = launch;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LaunchDetailFragment launchDetailFragment = new LaunchDetailFragment();
        ft.replace(R.id.mainOverviewFragment,launchDetailFragment);
        ft.addToBackStack("add");
        ft.commit();

    }

    public Launch getCurLaunch(){
        return curLaunch;
    }
}
