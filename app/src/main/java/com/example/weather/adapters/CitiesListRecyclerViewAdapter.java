package com.example.weather.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.example.weather.CityCard;
import com.example.weather.callBackInterfaces.IAdapterCallbacks;
import com.example.weather.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitiesListRecyclerViewAdapter extends RecyclerView.Adapter<CitiesListRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CityCard> data;
    private IAdapterCallbacks adapterCallbacks;

    public CitiesListRecyclerViewAdapter(ArrayList<CityCard> data, IAdapterCallbacks adapterCallbacks) {
        Log.d("CitiesListRVAdapter", "creating new adapter with " + data.toString());
        this.data = data;
        this.adapterCallbacks = adapterCallbacks;
    }

    public void addItem(CityCard cityCard) {
        data.add(0, cityCard);
        notifyItemInserted(0);
    }

    public void removeItem() {
        Log.d("CitiesListRVAdapter", "removingItem");
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
        holder.imageButton.setOnClickListener(view -> Snackbar.make(view, R.string.do_you_want_delete_city, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, view12 -> {
                    if (!data.isEmpty()) {
                        String city = holder.cityName.getText().toString();
                        Log.d("CitiesListRVAdapter", "deleting item " + city + " " + data.toString());
                        CityCard cityCard = new CityCard(city);
                        int pos = data.indexOf(cityCard);
                        data.remove(cityCard);
                        notifyItemRemoved(pos);
                        adapterCallbacks.saveList();
                    }
                }).show());
        holder.rippleView.setOnLongClickListener(view -> {
            Snackbar.make(view, "Удалить город?", Snackbar.LENGTH_LONG)
                    .setAction("Да", view1 -> {
                        if (!data.isEmpty()) {
                            String city = holder.cityName.getText().toString();
                            CityCard cityCard = new CityCard(city);
                            int pos = data.indexOf(cityCard);
                            data.remove(cityCard);
                            notifyItemRemoved(pos);
                        }
                    }).show();
            return true;
        });

        holder.cityName.setText(data.get(position).getCityName());

        holder.temp.setText(
                String.valueOf(
                        Math.round(data.get(position).getTemp())
                ).concat("°"));

        holder.feels_temp.setText(
                String.valueOf(
                        Math.round(data.get(position).getFeelsTemp())
                ).concat("°"));

        holder.icon.setText(String.valueOf(data.get(position).getIcon()));
        holder.rippleView.setOnClickListener(view -> adapterCallbacks.startWeatherFragment(holder.cityName.getText().toString()));

        adapterCallbacks.onAdapterUpdate();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cityTextViewOnCard)
        TextView cityName;
        @BindView(R.id.icon_card)
        TextView icon;
        @BindView(R.id.temp_card)
        TextView temp;
        @BindView(R.id.feels_like_temp_card)
        TextView feels_temp;
        @BindView(R.id.delete_city_btn)
        ImageButton imageButton;
        @BindView(R.id.rippleView)
        RippleView rippleView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Typeface weatherFont = Typeface.createFromAsset(Objects.requireNonNull(view.getContext()).getAssets(), "fonts/weathericons.ttf");
            icon.setTypeface(weatherFont);
        }
    }
}