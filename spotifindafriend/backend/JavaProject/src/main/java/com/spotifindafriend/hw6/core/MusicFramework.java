package com.spotifindafriend.hw6.core;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface MusicFramework {

    /**
     * Registers a new {@link DataPlugin} with the game framework
     * @param plugin the Data Plugin to register
     */
    void registerDataPlugin(DataPlugin plugin);

    /**
     * Registers a new {@link DataPlugin} with the game framework
     * @param plugin the Visualization Plugins to register
     */
    void registerVisPlugin(VisPlugin plugin);

    /**
     * Get all registered data plugins
     * @return a list of registered data plugins
     */
    List<DataPlugin> getDataPlugins();


    /**
     * Get all registered visual plugins
     * @return a list of registered visual plugins
     */
    List<VisPlugin> getVisPlugins();

    /**
     * Start a new session
     * @param plugin the data plugin we want to use
     * @param loginInfo the login information of the user using the dataplugin's music service
     */
    void startNewMusic(DataPlugin plugin, String loginInfo) throws IOException, ParseException, SpotifyWebApiException;

    /**
     * Creates the visualization HTML text
     * @param plugin the visualization plugin we want to use
     * @return the HTML text of the graph
     */
    String getVisualization(VisPlugin plugin);

    /**
     * Sets the currentPlugin
     * @param plugin the data plugin we want to set
     */
    void setCurrentPlugin(DataPlugin plugin);


    /**
     * Gets the currentPlugin
     * @return Plugin the data plugin we want to get
     */
    DataPlugin getCurrentPlugin();

    /**
     * Checks if the current plugin needs user to input their username
     * @return "true" if the plugin needs user's username, null otherwise
     */
    String getInput();


}
