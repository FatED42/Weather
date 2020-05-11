package com.example.weather;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Objects;

public class CityCard implements Parcelable {
    private int position;
    private String text;

    public CityCard(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CityCard cityCard = (CityCard) obj;
        return this.text.equalsIgnoreCase(cityCard.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
