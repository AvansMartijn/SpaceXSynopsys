package com.example.spacexsynopsis;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainOverviewFragment extends Fragment {
    private OnItemSelectListener onItemSelectListener;
    private ArrayList<Launch> launchList;
    private LaunchAdapter launchAdapter;

    public interface OnItemSelectListener {

        void onItemSelected(Launch launch);

        void onItemLongSelected(Launch launch);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_overview_fragment, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.main_overview_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //set fixed size for performance
        recyclerView.setHasFixedSize(true);

        launchAdapter = new LaunchAdapter(launchList);

        View.OnClickListener mMessageClickedHandler = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Launch launch = launchAdapter.getItem(itemPosition);
                onItemSelectListener.onItemSelected(launch);
            }
        };

        View.OnLongClickListener mLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Launch launch = launchAdapter.getItem(itemPosition);
                onItemSelectListener.onItemLongSelected(launch);
                return true;
            }
        };

        launchAdapter.setOnItemLongClickListener(mLongClick);
        launchAdapter.setOnItemClickListener(mMessageClickedHandler);
        recyclerView.setAdapter(launchAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onItemSelectListener = (OnItemSelectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
        launchList = new ArrayList<>();
    }

    public void addToLaunchList(Launch launch) {
        launchList.add(launch);
    }

    public int getLaunchListLength() {
        return launchList.size();
    }

    public void clearLaunchList() {
        if(launchList != null) {
            launchList.clear();
        }
    }

    public LaunchAdapter getLaunchAdapter() {
        return launchAdapter;
    }
}
