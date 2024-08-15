package com.spotifindafriend.hw6.dataplugins;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spotifindafriend.hw6.Song;
import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.MusicFramework;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LastFMPlugin implements DataPlugin {

    private List<Song> tracksList = new ArrayList<Song>();
    private List<String> genresList = new ArrayList<>();

    private MusicFramework framework;

    @Override
    public String getCode() {
        return "http://localhost:3000/lastfm";
    }

    @Override
    public boolean needsInput() {
        return true;
    }



    @Override
    public List<Song> getTopTracks(String loginInfo) throws IOException, ParseException, SpotifyWebApiException {
        String lastFMUrl = "http://ws.audioscrobbler.com/2.0/?method=user.gettoptracks&user=" + loginInfo +
                "&api_key=e7f5dece399a46e3e97c1a2bb4adfd1d&limit=30&period=6month&format=json";
        URL url = new URL(lastFMUrl);
        URLConnection request = url.openConnection();
        request.connect();

        Gson gson = new Gson();
        JsonArray tracks = new JsonArray();
        List<Song> topSongs = new ArrayList<Song>();

        // 1. JSON file to Java object
        JsonObject object = gson.fromJson(new InputStreamReader((InputStream) request.getContent()),
                JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = object.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            tracks = entry.getValue().getAsJsonObject().getAsJsonArray("track");
        }
        for (JsonElement t : tracks) {
            JsonObject track = t.getAsJsonObject();
            String songID = track.get("mbid").getAsString();
            String songName = track.get("name").getAsString().replace(" ", "+");
            String imageUrl = track.get("image")
                    .getAsJsonArray().get(0).getAsJsonObject().get("#text").getAsString();
            String artist = track.get("artist").getAsJsonObject().get("name")
                    .getAsString().replace(" ", "+");
            String artistID = track.get("artist").getAsJsonObject().get("mbid").getAsString();
            String trackURL = "http://ws.audioscrobbler.com/2.0/?method=" +
                    "track.getInfo&api_key=e7f5dece399a46e3e97c1a2bb4adfd1d&artist=" +
                    artist + "&track=" + songName + "&format=json";
            URL url2 = new URL(trackURL);
            URLConnection request2 = url2.openConnection();
            request2.connect();
            JsonObject trackInfo = gson.fromJson(new InputStreamReader((InputStream) request2.getContent()),
                    JsonObject.class).get("track").getAsJsonObject();
            Integer popularity = Integer.parseInt(trackInfo.get("playcount").getAsString());
            Song song = new Song(songName, artist, artistID, songID, imageUrl, popularity);
            topSongs.add(song);
        }
        this.tracksList = topSongs;
        return topSongs;
    }

    @Override
    public List<String> getTopGenres(String loginInfo) throws IOException, ParseException, SpotifyWebApiException {
        List<Song> topTracks = getTopTracks(loginInfo);
        List<String> allGenres = new ArrayList<String>();
        for (int i = 0; i < topTracks.size(); i++) {
            String genresURL = "http://ws.audioscrobbler.com/2.0/?" +
                    "method=track.gettoptags&api_key=e7f5dece399a46e3e97c1a2bb4adfd1d&artist=" +
                    topTracks.get(i).getArtist() + "&track=" + topTracks.get(i).getSong() + "&format=json";
            URL url = new URL(genresURL);
            URLConnection request = url.openConnection();
            request.connect();

            Gson gson = new Gson();
            JsonArray trackInfo = gson.fromJson(new InputStreamReader((InputStream) request.getContent()),
                    JsonObject.class).get("toptags").getAsJsonObject().get("tag").getAsJsonArray();
           topTracks.get(i).setSong(topTracks.get(i).getSong().replace("+", " "));
            topTracks.get(i).setArtist(topTracks.get(i).getArtist().replace("+", " "));
            for (int j = 0; j < trackInfo.size(); j++) {
                String genre = trackInfo.get(j).getAsJsonObject().get("name").getAsString();
                allGenres.add(genre);

            }
        }

        this.genresList = allGenres;
        return allGenres;
    }

    @Override
    public String getMusicServiceName() {
        return "Last FM";
    }

    @Override
    public List<Song> getTracksList() {
        return this.tracksList;
    }

    @Override
    public List<String> getGenresList() {
        return this.genresList;
    }

    @Override
    public void onRegister(MusicFramework m) {
        framework = m;
    }
}
