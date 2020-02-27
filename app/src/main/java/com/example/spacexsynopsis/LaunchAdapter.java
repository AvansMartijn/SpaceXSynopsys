package com.example.spacexsynopsis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LaunchAdapter extends RecyclerView.Adapter<LaunchAdapter.LaunchViewHolder>{
    private List<Launch> launches;
    private View.OnClickListener onItemClickListener;
    private View.OnLongClickListener onItemLongClickListener;

    public LaunchAdapter(List<Launch> list) {
        launches = list;
    }

    @Override
    public LaunchViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.launch_item, parent, false);

        LaunchViewHolder launchViewHolder = new LaunchViewHolder(view);
        launchViewHolder.itemView.setOnClickListener(onItemClickListener);
        launchViewHolder.itemView.setOnLongClickListener(onItemLongClickListener);

        return launchViewHolder;
    }

    @Override
    public void onBindViewHolder(LaunchViewHolder holder, int position)
    {
        holder.missionNameTextView.setText(launches.get(position).getName());
        holder.missionDateTextView.setText(launches.get(position).getDate());
        holder.missionPatchImageView.setImageBitmap(launches.get(position).getMissionPatch());
    }



    @Override
    public int getItemCount()
    {
        return launches.size();
    }

    public Launch getItem(int adapterPosition) {
        return launches.get(adapterPosition);
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        onItemClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener){
        onItemLongClickListener = onLongClickListener;
    }

    class LaunchViewHolder extends RecyclerView.ViewHolder
    {
        ImageView missionPatchImageView;
        TextView missionNameTextView;
        TextView missionDateTextView;

        LaunchViewHolder(View view)
        {
            super(view);
            missionPatchImageView = view.findViewById(R.id.image_view_mission_patch);
            missionNameTextView = view.findViewById(R.id.text_view_mission_name);
            missionDateTextView = view.findViewById(R.id.text_view_mission_date);
            itemView.setTag(this);
        }

    }
}
