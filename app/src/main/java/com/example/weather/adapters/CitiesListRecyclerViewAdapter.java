package com.example.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.CityCard;
import com.example.weather.callBackInterfaces.IAdapterCallbacks;
import com.example.weather.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CitiesListRecyclerViewAdapter extends RecyclerView.Adapter<CitiesListRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CityCard> data = new ArrayList<>();
    private IAdapterCallbacks adapterCallbacks;

    public CitiesListRecyclerViewAdapter(ArrayList<CityCard> data,IAdapterCallbacks adapterCallbacks) {
        if (data != null) {
            this.data = data;
        }
        this.adapterCallbacks = adapterCallbacks;
    }

    public void addItem(CityCard cityCard) {
        data.add(0, cityCard);
        notifyItemInserted(0);
    }

    public void removeItem() {
        if (!data.isEmpty()) {
            data.remove(0);
            notifyItemRemoved(0);
        }
    }

    public boolean checkIsItemInData(String city) {
        CityCard cityCard = new CityCard(city);
        return data.contains(cityCard);
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
        holder.textView.setText(data.get(position).getText());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, R.string.do_you_want_delete_city, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!data.isEmpty()) {
                                    String city = holder.textView.getText().toString();
                                    CityCard cityCard = new CityCard(city);
                                    int pos = data.indexOf(cityCard);
                                    data.remove(cityCard);
                                    notifyItemRemoved(pos);
                                }
                            }
                        }).show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, R.string.do_you_want_delete_city, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!data.isEmpty()) {
                                    String city = holder.textView.getText().toString();
                                    CityCard cityCard = new CityCard(city);
                                    int pos = data.indexOf(cityCard);
                                    data.remove(cityCard);
                                    notifyItemRemoved(pos);
                                }
                            }
                        }).show();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        ImageButton imageButton;

        ViewHolder(View view) {
            super(view);
            imageButton = itemView.findViewById(R.id.delete_city_btn);
            textView = itemView.findViewById(R.id.cityTextViewOnCard);
        }
    }
}