package com.example.spacexsynopsis;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class LaunchDetailFragment extends Fragment {
    private static final String NAME = "name";
    private static final String DATE = "date";
    private static final String DETAILS = "details";
    private static final String LAUNCHSITE = "launchsite";
    private static final String ROCKETNAME = "rocketname";

    private String mName;
    private String mDate;
    private String mLaunchSite;
    private String mRocketName;
    private String mDetails;
    private OnFragmentInteractionListener mListener;

    public LaunchDetailFragment() {
        // Required empty public constructor
    }

    public static LaunchDetailFragment newInstance(Launch launch) {
        LaunchDetailFragment fragment = new LaunchDetailFragment();
        Bundle args = new Bundle();
        args.putString(NAME, launch.getName());
        args.putString(DATE, launch.getDate());
        args.putString(DETAILS, launch.getLaunchDetails());
        args.putString(LAUNCHSITE, launch.getLaunchSiteName());
        args.putString(ROCKETNAME, launch.getRocketName());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
            mDate = getArguments().getString(DATE);
            mDetails = getArguments().getString(DETAILS);
            mLaunchSite = getArguments().getString(LAUNCHSITE);
            mRocketName = getArguments().getString(ROCKETNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launch_detail_fragment, container, false);
        TextView tvName = view.findViewById(R.id.launch_detail_name);
        TextView tvDate = view.findViewById(R.id.launch_detail_date);
        TextView tvDetails = view.findViewById(R.id.launch_detail_details);
        TextView tvRocketName = view.findViewById(R.id.launch_detail_rocket_name);
        TextView tvLaunchSite = view.findViewById(R.id.launch_detail_launch_site);

        tvName.setText(mName);
        tvDate.setText(mDate);
        tvDetails.setText(mDetails);
        tvRocketName.setText(mRocketName);
        tvLaunchSite.setText(mLaunchSite);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
