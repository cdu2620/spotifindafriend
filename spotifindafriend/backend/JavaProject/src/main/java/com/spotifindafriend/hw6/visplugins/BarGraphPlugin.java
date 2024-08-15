package com.spotifindafriend.hw6.visplugins;

import com.spotifindafriend.hw6.Song;
import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.VisPlugin;
import org.apache.hc.core5.http.ParseException;
import org.icepear.echarts.Bar;
import org.icepear.echarts.render.Engine;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class BarGraphPlugin implements VisPlugin {

    private List<Song> topSongs;
    private String[] songNames;
    private Number[] popularityScores;

    public BarGraphPlugin(DataPlugin data) throws IOException, ParseException, URISyntaxException, SpotifyWebApiException {
        topSongs = data.getTracksList();
        songNames = getSongNames();
        popularityScores = getPopularityScores();
    }

    private String[] getSongNames() {
        String[] songNames = new String[10];
        for (int i = 0; i < 10; i ++) {
            songNames[i] = topSongs.get(i).getSong();
        }
        return songNames;
    }

    private Number[] getPopularityScores() {
        Number[] pop = new Number[10];
        for (int i = 0; i < 10; i ++) {
            pop[i] = topSongs.get(i).getPopularity();
        }
        return pop;
    }


    public String makeVisualization() {
        // All methods in ECharts Java supports method chaining
        Bar bar = new Bar()
                .setLegend()
                .setTitle("Your Top 10 Tracks vs. Popularity")
                .setTooltip("item")
                .addXAxis("Songs", songNames)
                .addYAxis("Popularity Score")
                .addSeries("popularity", popularityScores);
        Engine engine = new Engine();

        return engine.renderHtml(bar);
    }

}
