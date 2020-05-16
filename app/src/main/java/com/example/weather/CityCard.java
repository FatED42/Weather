package com.example.weather;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class CityCard implements Parcelable {
    private int position;
    Drawable drawable;
    String text;

    CityCard(Drawable drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }

    private CityCard(Parcel in) {
        position = in.readInt();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CityCard> CREATOR = new Creator<CityCard>() {
        @Override
        public CityCard createFromParcel(Parcel in) {
            return new CityCard(in);
        }

        @Override
        public CityCard[] newArray(int size) {
            return new CityCard[size];
        }
    };
}
