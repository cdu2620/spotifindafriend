package com.spotifindafriend.hw6.visplugins;

import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.VisPlugin;
import org.apache.hc.core5.http.ParseException;
import org.icepear.echarts.Bar;
import org.icepear.echarts.Pie;
import org.icepear.echarts.charts.pie.PieDataItem;
import org.icepear.echarts.render.Engine;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PieChartPlugin implements VisPlugin {

    private List<String> genres;

    public PieChartPlugin(DataPlugin data) throws IOException, ParseException, SpotifyWebApiException {
        genres = data.getGenresList();
    }


    private PieDataItem[] createPieData() {
        Map<String, Long> map = genres.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        List<Map.Entry<String, Long>> result = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toList());
        double total = 0;
        for (int i = 0; i < 10; i ++) {
            total += result.get(i).getValue();
        }

        PieDataItem[] pie = new PieDataItem[10];
        for (int i = 0; i < 10; i ++) {
            pie[i] = new PieDataItem().setValue(result.get(i).getValue() / total).setName(result.get(i).getKey());
        }

        return pie;
    }

    @Override
    public String makeVisualization() {
        Pie pie = new Pie()
                .setTitle("Your Top 10 Genres")
                .setTooltip("item")
                .setLegend()
                .addSeries(createPieData());
        Engine engine = new Engine();
        return engine.renderHtml(pie);
//        engine.render("pie.html", pie);
    }

}
