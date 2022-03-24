package com.example.notebooks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private String documentId;
    private String title;
    private String timeCreate;
    private String lastTimeUpdated;
    private String content;
    private String tag;

    public Note() {
    }

    public Note(String documentId, String title, String content, String timeCreate, String lastTimeUpdated, String tag) {
        this.documentId = documentId;
        this.title = title;
        this.timeCreate = timeCreate;
        this.lastTimeUpdated = lastTimeUpdated;
        this.content = content;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public String getLastTimeUpdated() {
        return lastTimeUpdated;
    }

    public String getContent() {
        return content;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLastTimeUpdated(String lastTimeUpdated) {
        this.lastTimeUpdated = lastTimeUpdated;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.documentId);
        dest.writeString(this.title);
        dest.writeString(this.timeCreate);
        dest.writeString(this.lastTimeUpdated);
        dest.writeString(this.content);
        dest.writeString(this.tag);
    }

    protected Note(Parcel in) {
        this.documentId = in.readString();
        this.title = in.readString();
        this.timeCreate = in.readString();
        this.lastTimeUpdated = in.readString();
        this.content = in.readString();
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
