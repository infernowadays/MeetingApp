package com.lazysecs.walk.models;

import com.google.gson.annotations.SerializedName;

public class ShortChat {
    @SerializedName("content_id")
    private int contentId;
    @SerializedName("last_message_id")
    private int lastMessageId;
    @SerializedName("last_seen_message_id")
    private int lastSeenMessageId;

    public ShortChat(int contentId, int lastMessageId, int lastSeenMessageId) {
        this.contentId = contentId;
        this.lastMessageId = lastMessageId;
        this.lastSeenMessageId = lastSeenMessageId;
    }

    public ShortChat() {
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public int getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getLastSeenMessageId() {
        return lastSeenMessageId;
    }

    public void setLastSeenMessageId(int lastSeenMessageId) {
        this.lastSeenMessageId = lastSeenMessageId;
    }

}
