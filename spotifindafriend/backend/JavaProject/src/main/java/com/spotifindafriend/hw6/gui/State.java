package com.spotifindafriend.hw6.gui;

import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.MusicFrameworkImpl;

import java.util.Arrays;
import java.util.List;

public final class State {

        private final Plugin[] plugins;
        private final String userInput;

        private State(Plugin[] plugins, String userInput) {

            this.plugins = plugins;
            this.userInput = userInput;

        }

        public static State forGame(MusicFrameworkImpl music) {
            Plugin[] plugins = getPlugins(music);
            String input = music.getInput();
            return new State(plugins, input);
        }

        private static Plugin[] getPlugins(MusicFrameworkImpl music) {
            List<DataPlugin> allPlugins = music.getDataPlugins();
            Plugin[] plugins = new Plugin[allPlugins.size()];
            for (int i=0; i<allPlugins.size(); i++){
                String link = allPlugins.get(i).getCode();
                plugins[i] = new Plugin(allPlugins.get(i).getMusicServiceName(), link);
            }
            return plugins;
        }

    public String getUserInput() {
        return userInput;
    }

    public Plugin[] getPlugins() {
        return plugins;
    }

        @Override
        public String toString() {
            return "State{" +
                    ", plugins=" + Arrays.toString(plugins) +
                    ", userInput='" + userInput + '\'' +
                    '}';
        }

}



