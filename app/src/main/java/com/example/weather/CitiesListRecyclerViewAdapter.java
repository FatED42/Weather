package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CitiesListRecyclerViewAdapter extends RecyclerView.Adapter<CitiesListRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CityCard> data = new ArrayList<>();
    private IAdapterCallbacks adapterCallbacks;
    private int currentPosition;

    CitiesListRecyclerViewAdapter(ArrayList<CityCard> data, IAdapterCallbacks adapterCallbacks) {
        if (data != null) {
            this.data = data;
        }
        this.adapterCallbacks = adapterCallbacks;
    }

    void addItem(CityCard cityCard) {
        data.add(0, cityCard);
        notifyItemInserted(0);
    }

    void removeItem() {
        if (!data.isEmpty()) {
            data.remove(0);
            notifyItemRemoved(0);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                .inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position).text);
        holder.imageView.setImageDrawable(data.get(position).drawable);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                adapterCallbacks.startWeatherFragment(holder.textView.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = itemView.findViewById(R.id.imageViewOnCard);
            textView = itemView.findViewById(R.id.cityTextViewOnCard);
        }
    }
}