# SpotiStats

## Introduction
SpotiStats provides statistical analysis and visualizations of users’ music tastes. This framework lets users to log in with their preferred music service then generates visualizations of their listening histories.

## How To Use
Open the project in IntelliJ and run the `main` function in `App.java`. Then go to `localhost:3000` to see the UI. The app first prompts the user to pick a music service. Once the user is logged in, they can pick whether to generate a bar graph or a pie chart. Then they can view their listening statistics in the forms of graphs.

**Note: In order to login with your Spotify account, the email associated with your Spotify account must be added to our developer portal. As per the Spotify docmentation, since our app is in Developer mode, "Up to 25 Spotify users can install and use your app. These users must be explicitly added under the section "Users and Access" before they can authenticate with your app."**

## Framework Details
The framework's main job is to register various plugins and tells the frontend which data plugin it is currently using. Specifically, after the user picks which streaming service they want on the browser, the framework will take in that information and sets the current plugin correctly. 

## Data Plugin Details
The data plugins are for various music streaming services. Currently, we have a Spotify plugin and a LastFM plugin. Each plugin implements a data plugin interface, which requires the following methods:

- `needsInput()`: whether or not the plugin need user to enter their username or any other authentication requirements
- `getCode()`: gets the authentication code for the music service
- `getTopTracks(loginInfo)`: given the user's login info, get the 10 most listened to tracks in a list of Song objects
- `getTopGenres(loginInfo)`: given the user's login info, get the 10 most listened to genres in a list of Strings
- `getTracksList()`: get the 10 most listened to tracks in a list of Song objects (this method does not require login information to decrease coupling when used with visualization plugin)
- `getGenresList()`: get the 10 most listened to genres in a list of String (this method does not require login information to decrease coupling when used with visualization plugin)
- `getMusicServiceName()`: get the name of the music service
- `onRegister()`: actions to perform when registered with a framework

Both `getTopTracks(loginInfo)` and `getTracksList()` returns a list of Song objects. A `Song` object includes the song name, artist name, artist ID, song ID, url of song image, and a popularity score.


<ins>How to Extend</ins>

To extend a data plugin, programmers can pick another music streaming service and implement the above methods. Most music streaming services have APIs that allow a user to authenticate and get their listened to tracks/genres. The programmer can make use of these APIs and create another data plugin class that implements this interface.

## Visualization Plugin Details

The visualization plugins are for creating various graphs. Currently, we have a Bar Graph plugin and a Pie Chart plugin. The Bar Graph creates a graph of the user's 10 most listened to tracks vs. the track's popularity. The Pie Chart creates a pie chart of the user's 10 most listened to genres. Each plugin implements a VisPlugin interface which requires the `makeVisualization()` method that returns the HTML text of the visualization.

Currently, the visualization plugin takes in a data plugin to know which music streaming service it is working with, then gets the information it needs (like top tracks or top genres) from the data plugins. The vis plugins then uses an external library called ECharts to produce the appropriate graphs.  

<ins>How to Extend</ins>

To create a new visualization plugin, programmers can find another type of plot to produce. The programmers can create a new vis plugin class that implements the interface. Within the class, they can create the graph they want using existing information produced by the data plugins. 
