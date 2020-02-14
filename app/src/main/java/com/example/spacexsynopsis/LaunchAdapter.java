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

        return launchViewHolder;
    }

    @Override
    public void onBindViewHolder(LaunchViewHolder holder, int position)
    {
        holder.textView.setText(launches.get(position).name);
        holder.imageView.setImageResource(launches.get(position).picture);
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

    class LaunchViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        LaunchViewHolder(View view)
        {
            super(view);

            imageView = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            itemView.setTag(this);
        }

    }
}
