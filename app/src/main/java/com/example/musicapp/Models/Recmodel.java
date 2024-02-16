package com.example.musicapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Recmodel implements Parcelable {
    String pic;
    String songname;
    String songurl;
    String song_artist;

    public Recmodel(String pic, String songname, String songurl, String song_artist) {
        this.pic = pic;
        this.songname = songname;
        this.songurl=songurl;
        this.song_artist=song_artist;
    }

    public static final Creator<Recmodel> CREATOR = new Creator<Recmodel>() {
        @Override
        public Recmodel createFromParcel(Parcel in) {
            return new Recmodel(in);
        }

        @Override
        public Recmodel[] newArray(int size) {
            return new Recmodel[size];
        }
    };

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongurl() {
        return songurl;
    }

    public void setSongurl(String songurl) {
        this.songurl = songurl;
    }
    public String getSong_artist() {
        return song_artist;
    }

    public void setSong_artist(String song_artist) {
        this.song_artist = song_artist;
    }

    public Recmodel(@NonNull Parcel in) {
        pic = in.readString();
        songname = in.readString();
        songurl=in.readString();
        song_artist=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(pic);
        parcel.writeString(songname);
        parcel.writeString(songurl);
        parcel.writeString(song_artist);
    }
}
