package WeatherAPP;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * The WeatherAPI class is responsible for fetching weather data from an external API.
 * It encapsulates the logic required to send HTTP requests and handle responses.
 */

public class WeatherAPI {

    // Constants for the API base URL and the API key
    private static final String BASE_URL = "https://api.openweathermap.org/data/3.0/onecall";
    private static final String API_KEY = "*******************"; // 

    /**
     * Fetches weather data for a specific geographic location using OpenWeatherMap API.
     *
     * @param latitude  The latitude of the location for which weather data is to be retrieved.
     * @param longitude The longitude of the location for which weather data is to be retrieved.
     * @return A string containing the weather data in JSON format, or {@code null} if an error occurs.
     */
    public String getWeatherData(double latitude, double longitude) {
        // Construct the URL with the given coordinates and API key
        String url = BASE_URL + "?lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;

        // Create an HttpClient and an HttpRequest
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Send the request and return the response body
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            // Handle exceptions and return null or an appropriate error message
            e.printStackTrace();
            return null;
        }
    }
}
