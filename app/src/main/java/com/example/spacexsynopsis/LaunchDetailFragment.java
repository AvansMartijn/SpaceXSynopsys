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

    private String mName;
    private String mDate;

    private OnFragmentInteractionListener mListener;

    private TextView textView;
    private TextView textViewDate;

    public LaunchDetailFragment() {
        // Required empty public constructor
    }


    public static LaunchDetailFragment newInstance(String name, String date) {
        LaunchDetailFragment fragment = new LaunchDetailFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(NAME);
            mDate = getArguments().getString(DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.launch_detail_fragment, container, false);
        textView = view.findViewById(R.id.textView);
        textViewDate = view.findViewById(R.id.launch_detail_date);
        textView.setText(mName);
        textViewDate.setText(mDate);

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
