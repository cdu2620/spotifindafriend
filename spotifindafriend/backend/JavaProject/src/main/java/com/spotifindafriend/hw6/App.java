package com.spotifindafriend.hw6;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.MusicFrameworkImpl;
import com.spotifindafriend.hw6.core.VisPlugin;
import com.spotifindafriend.hw6.dataplugins.LastFMPlugin;
import com.spotifindafriend.hw6.dataplugins.SpotifyPlugin;
import com.spotifindafriend.hw6.gui.State;
import com.spotifindafriend.hw6.visplugins.BarGraphPlugin;
import com.spotifindafriend.hw6.visplugins.PieChartPlugin;
import fi.iki.elonen.NanoHTTPD;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }}

    private MusicFrameworkImpl music;
    private Template template;
    private List<DataPlugin> dataPlugins = new ArrayList<DataPlugin>();

    public App() throws IOException {
        super(3000);

        this.music = new MusicFrameworkImpl();
        dataPlugins.add(new SpotifyPlugin());
        dataPlugins.add(new LastFMPlugin());
        for (int i = 0; i < dataPlugins.size(); i++) {
            music.registerDataPlugin(dataPlugins.get(i));
        }

        Handlebars handlebars = new Handlebars();
        this.template = handlebars.compile("game_template");

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

       try {
           if (uri.equals("/loginSuccess")) {
               if (params.get("code").length() == 199) {
                   music.setCurrentPlugin(dataPlugins.get(0));
               }
               else {
                   music.setCurrentPlugin(dataPlugins.get(1));
               }
               music.startNewMusic(music.getCurrentPlugin(), params.get("code"));
               Handlebars handlebars = new Handlebars();
               String html = "";
               try {
                   Template template = handlebars.compile("vis");
                   html = template.apply("abc");
               } catch (IOException e) {
                   System.out.println("template file not found");
               }
               return newFixedLengthResponse(html);
        }
         else if (uri.equals("/lastfm")) {
             music.setCurrentPlugin(dataPlugins.get(1));
        } else if (uri.equals("/visual")) {
               List <VisPlugin> visPlugins = new ArrayList<VisPlugin>();
               visPlugins.add(new BarGraphPlugin(music.getCurrentPlugin()));
               visPlugins.add(new PieChartPlugin(music.getCurrentPlugin()));
               Handlebars handlebars = new Handlebars();
               String html = "";
               try {
                   Template template = handlebars.compile("index1");
                   html = template.apply(music.getVisualization(
                           visPlugins.get(Integer.parseInt(params.get("x")))));
               } catch (IOException e) {
                   System.out.println("template file not found");
               }
               return newFixedLengthResponse(html);
           }
        // Extract the view-specific data from the game and apply it to the template.
        State gameplay = State.forGame(this.music);
        String html = this.template.apply(gameplay);
        return newFixedLengthResponse(html); }
       catch (IOException e) {
           e.printStackTrace();
           return null;
       } catch (ParseException e) {
           e.printStackTrace();
           return null;
       } catch (SpotifyWebApiException e) {
           e.printStackTrace();
           return null;
       } catch (URISyntaxException e) {
           e.printStackTrace();
           return null;
       }
    }


}

