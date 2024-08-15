package com.spotifindafriend.hw6;

public class Song {

    private String song;
    private String artist;
    private String artistID;
    private String songID;
    private String imgUrl;
    private Integer popularity;

    public Song(String song, String artist, String artistID, String songID, String img, Integer popularity) {

            this.song = song;
            this.artist = artist;
            this.artistID = artistID;
            this.songID = songID;
            this.imgUrl = img;
            this.popularity = popularity;
    }

    public String getSong() {
        return this.song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistID() {
        return this.artistID;
    }

    public void setArtistID(String artistID) {
        this.artist = artistID;
    }

    public String getSongID() {
        return this.songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public Integer getPopularity() {
        return this.popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}

