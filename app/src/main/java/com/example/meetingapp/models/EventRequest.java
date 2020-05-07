package com.example.meetingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class EventRequest implements Parcelable {

    public static final Parcelable.Creator<EventRequest> CREATOR = new Parcelable.Creator<EventRequest>() {
        public EventRequest createFromParcel(Parcel in) {
            return new EventRequest(in);
        }

        public EventRequest[] newArray(int size) {
            return new EventRequest[size];
        }
    };
    @SerializedName("from_user")
    private String fromUser;
    @SerializedName("to_user")
    private String toUser;
    @SerializedName("event")
    private long event;
    private String decision;
    private boolean seen;

    public EventRequest(String fromUser, String toUser, long event) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.event = event;
    }

    public EventRequest() {
    }

    private EventRequest(Parcel in) {
        fromUser = in.readString();
        toUser = in.readString();
        event = in.readLong();
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromUser);
        dest.writeString(toUser);
        dest.writeLong(event);
    }
}
