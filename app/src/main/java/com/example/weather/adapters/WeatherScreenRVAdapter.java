package com.example.weather.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.ForecastCard;
import com.example.weather.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherScreenRVAdapter extends RecyclerView.Adapter<WeatherScreenRVAdapter.ViewHolder> {

    public WeatherScreenRVAdapter(ArrayList<ForecastCard> data) {
        if (data != null) this.data = data;
    }

    private ArrayList<ForecastCard> data = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.card_temp_screen_forecast_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.day.setText(data.get(position).getDate());
        holder.icon.setText(data.get(position).getIcon());
        holder.tempValue.setText(
                String.valueOf(
                        Math.round(data.get(position).getTemp())
                ).concat("°"));
        holder.tempValueEvening.setText(
                String.valueOf(
                        Math.round(data.get(position).getTempNight())
                ).concat("°"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.temp_screen_card_dayTV)
        TextView day;
        @BindView(R.id.temp_screen_card_iconTV)
        TextView icon;
        @BindView(R.id.temp_screen_card_tempValueTV)
        TextView tempValue;
        @BindView(R.id.temp_screen_card_tempValueEveningTV)
        TextView tempValueEvening;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface weatherFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/weathericons.ttf");
            icon.setTypeface(weatherFont);
        }
    }

}
