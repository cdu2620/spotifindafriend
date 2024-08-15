package com.spotifindafriend.hw6.core;

import com.spotifindafriend.hw6.Song;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface DataPlugin {

    /**
     * Whether the data plugin needs user to input their username
     * @return true if data plugin needs user input, false otherwise
     */
    boolean needsInput();

    /**
     * Get the authorization code for the music service
     * @return the authorization code
     */
    String getCode();

    /**
     * Get the top 10 listened to tracks for the user with their login credentials
     * @param loginInfo the login information of the user
     * @return list of 10 song object
     */
    List<Song> getTopTracks(String loginInfo) throws IOException, ParseException, URISyntaxException, SpotifyWebApiException;

    /**
     * Get the top 10 most listened to genres for the user with their login credentials
     * @param loginInfo the login information of the user
     * @return list of 10 genre strings
     */
    List<String> getTopGenres(String loginInfo) throws IOException, ParseException, SpotifyWebApiException;

    /**
     * Get the top 10 listened to tracks for the user without their login credentials
     * @return list of 10 song object
     */
    List<Song> getTracksList();

    /**
     * Get the top 10 most listened to genres for the user without their login credentials
     * @return list of 10 genre strings
     */
    List<String> getGenresList();

    /**
     * Return the name of the music service
     * @return the name of the music service
     */
    String getMusicServiceName();

    /**
     * The actions taken when the plugin is registered with the framework.
     * For now simply set the framework attribute in the plugin
     * @param framework the framework to register the plugins with
     */
    void onRegister(MusicFramework framework);


}
