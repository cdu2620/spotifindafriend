package com.spotifindafriend.hw6;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.spotifindafriend.hw6.core.DataPlugin;
import com.spotifindafriend.hw6.core.MusicFrameworkImpl;
import com.spotifindafriend.hw6.core.VisPlugin;
import com.spotifindafriend.hw6.dataplugins.LastFMPlugin;
import com.spotifindafriend.hw6.dataplugins.SpotifyPlugin;
import com.spotifindafriend.hw6.visplugins.BarGraphPlugin;
import org.apache.hc.core5.http.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Unit test for simple App.
 */
public class FrameworkTest
{
    /**
     * Rigorous Test :-)
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWithInvalidLoginSpotify() throws IOException,
            ParseException, SpotifyWebApiException {

        expectedException.expect(SpotifyWebApiException.class);
        MusicFrameworkImpl test = new MusicFrameworkImpl();
        test.startNewMusic(new SpotifyPlugin(), "wrong_info");

    }
    @Test
    public void shouldThrowExceptionWithInvalidLoginLastFM() throws IOException,
            ParseException, SpotifyWebApiException, URISyntaxException {

        expectedException.expect(IOException.class);
        MusicFrameworkImpl test = new MusicFrameworkImpl();
        test.startNewMusic(new LastFMPlugin(), "wrong_info");
        expectedException.expect(URISyntaxException.class);
        BarGraphPlugin bar = new BarGraphPlugin(test.getCurrentPlugin());
        assertEquals(test.getVisualization(bar), "");

    }

    @Test
    public void shouldBeSuccessfulWithValidUser() throws IOException,
            ParseException, SpotifyWebApiException, URISyntaxException {

        MusicFrameworkImpl test = new MusicFrameworkImpl();
        test.startNewMusic(new LastFMPlugin(), "cdu2620");
        assertEquals(test.getCurrentPlugin().getTracksList().size(), 30);
        BarGraphPlugin bar = new BarGraphPlugin(test.getCurrentPlugin());
        assertTrue(test.getVisualization(bar).startsWith("<html>"));

    }

    @Test
    public void testGetVisualization() {
        VisPlugin bar = mock(VisPlugin.class);
        MusicFrameworkImpl test = new MusicFrameworkImpl();
        test.getVisualization(bar);
        verify(bar).makeVisualization();
    }
}
