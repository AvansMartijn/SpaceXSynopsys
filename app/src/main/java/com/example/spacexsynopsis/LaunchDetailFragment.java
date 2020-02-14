package com.example.spacexsynopsis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LaunchDetailFragment extends Fragment {
    Launch launch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launch_detail_fragment, container, false);
        TextView textView = view.findViewById(R.id.textView2);
        MainActivity mainActivity = (MainActivity) getActivity();
        Launch launch = mainActivity.getCurLaunch();
        textView.setText(launch.name);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
