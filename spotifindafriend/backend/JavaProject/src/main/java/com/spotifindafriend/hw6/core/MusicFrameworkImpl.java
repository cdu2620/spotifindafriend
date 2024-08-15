package com.spotifindafriend.hw6.core;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicFrameworkImpl implements MusicFramework {

    private List<DataPlugin> registeredDataPlugins;
    private List<VisPlugin> registeredVisPlugins;
    private DataPlugin currentPlugin;

    /**
     * Instantiates a new Music Framework
     */
    public MusicFrameworkImpl() {
        registeredDataPlugins = new ArrayList<DataPlugin>();
    }

    /**
     * Registers a new {@link DataPlugin} with the game framework
     * @param plugin the Data Plugin to register
     */
    public void registerDataPlugin(DataPlugin plugin) {
        plugin.onRegister(this);
        registeredDataPlugins.add(plugin);
    }

    /**
     * Registers a new {@link DataPlugin} with the game framework
     * @param plugin the Visualization Plugins to register
     */
    public void registerVisPlugin(VisPlugin plugin) {
        registeredVisPlugins.add(plugin);
    }

    /**
     * Get all registered data plugins
     * @return a list of registered data plugins
     */
    public List<DataPlugin> getDataPlugins() {
        return this.registeredDataPlugins;
    }


    /**
     * Get all registered visual plugins
     * @return a list of registered visual plugins
     */
    public List<VisPlugin> getVisPlugins() {
        return this.registeredVisPlugins;
    }

    /**
     * Start a new session
     * @param plugin the data plugin we want to use
     * @param loginInfo the login information of the user using the dataplugin's music service
     */
    public void startNewMusic(DataPlugin plugin, String loginInfo) throws IOException, ParseException, SpotifyWebApiException {
        currentPlugin = plugin;
        currentPlugin.getTopGenres(loginInfo);
    }

    public String getVisualization(VisPlugin plugin) {
        return plugin.makeVisualization();
    }

    /**
     * Sets the currentPlugin
     * @param plugin the data plugin we want to set
     */
    public void setCurrentPlugin(DataPlugin plugin) {
        this.currentPlugin = plugin;
    }


    /**
     * Gets the currentPlugin
     * @return Plugin the data plugin we want to get
     */
    public DataPlugin getCurrentPlugin() {
        return this.currentPlugin;
    }

    /**
     * Checks if the current plugin needs user to input their username
     * @return "true" if the plugin needs user's username, null otherwise
     */
    public String getInput() {
        if (currentPlugin == null) {
            return null;
        }
        if (currentPlugin.needsInput()) {
            return "true";
        }
        return null;
    }
}
