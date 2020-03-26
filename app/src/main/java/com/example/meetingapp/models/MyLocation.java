package com.example.meetingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MyLocation implements Parcelable {
    private double latitude;
    private double longitude;
    private String address;

    public MyLocation(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
    }

    public static final Parcelable.Creator<MyLocation> CREATOR = new Parcelable.Creator<MyLocation>() {
        public MyLocation createFromParcel(Parcel in) {
            return new MyLocation(in);
        }

        public MyLocation[] newArray(int size) {
            return new MyLocation[size];
        }
    };

    private MyLocation(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
    }
}
