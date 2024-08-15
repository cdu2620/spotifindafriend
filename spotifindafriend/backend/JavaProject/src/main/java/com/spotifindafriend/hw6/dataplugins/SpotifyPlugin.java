package com.spotifindafriend.hw6.dataplugins;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.spotifindafriend.hw6.Song;
import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.MusicFramework;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;


import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class SpotifyPlugin implements DataPlugin {

    private SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("7916ac8be5134a009b128dd5f63c8ce6")
                .setClientSecret("1178f86e91f64c1c9329241f6a2769d9")
                .setRedirectUri(URI.create("http://localhost:3000/loginSuccess"))
            .build();
    private AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .scope("user-top-read")
            .build();

    private List<Song> tracksList = new ArrayList<Song>();
    private List<String> genresList = new ArrayList<>();
    private MusicFramework framework;

    @Override
    public boolean needsInput() {
        return false;
    }

    @Override
    public String getCode() {
        URI uri = authorizationCodeUriRequest.execute();
        // need to actually get the code sigh
        return uri.toString();
    }

    public String getAccessTokenSpotify(String code) throws IOException, ParseException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        // Set access and refresh token for further "spotifyApi" object usage
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        return spotifyApi.getAccessToken();
    }



    @Override
    public List<Song> getTopTracks(String loginInfo) throws IOException, ParseException, SpotifyWebApiException {
        List<Song> topSongs = new ArrayList<Song>();
        String tmp = getAccessTokenSpotify(loginInfo);
        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
          .limit(30)
          .offset(0)
          .time_range("medium_term")
                .build();

        Gson gson = new Gson();
        JsonArray topTracks = gson.fromJson(getUsersTopTracksRequest.getJson(),
                JsonObject.class).get("items").getAsJsonArray();
        for (int i = 0; i < topTracks.size(); i++) {
            JsonObject tracks = topTracks.get(i).getAsJsonObject();
            String songName = tracks.get("name").getAsString();
            String songID = tracks.get("id").getAsString();
            String artistName = tracks.get("artists").
                    getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String artistID = tracks.get("artists").
                    getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
            String imgUrl = tracks.get("album").getAsJsonObject().get("images").
                    getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
            Integer popularity = Integer.parseInt(tracks.get("popularity").getAsString());
            Song song = new Song(songName, artistName, artistID, songID, imgUrl, popularity);
            topSongs.add(song);
        }
        this.tracksList = topSongs;
        return topSongs;
    }

    @Override
    public List<String> getTopGenres(String loginInfo) throws IOException, ParseException, SpotifyWebApiException {
        List<String> allGenres = new ArrayList<>();
        List<Song> topTracks = getTopTracks(loginInfo);
        for (int i = 0; i < topTracks.size(); i++){
            String id = topTracks.get(i).getArtistID();
            GetArtistRequest getArtistRequest = spotifyApi.getArtist(id)
                    .build();
            Artist artist = getArtistRequest.execute();
            String[] genres = artist.getGenres();
            for (String genre: genres) {
                allGenres.add(genre);
            }
        }
        this.genresList = allGenres;
        return allGenres;
    }

    @Override
    public String getMusicServiceName() {
        return "Spotify";
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
