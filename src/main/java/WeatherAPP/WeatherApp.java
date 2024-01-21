package WeatherAPP;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The WeatherApp class represents a weather dashboard application using JavaFX.
 * It provides functionality to display weather information based on selected cities or manually entered coordinates.
 */

public class WeatherApp extends Application {

    // declaration for UI components
    private ComboBox<String> citySelection;
    private Button refreshButton, confirmButton, graphButton;
    private TextField latitudeInput, longitudeInput;
    private Label manualEntryErrorLabel;
    private Label lastUpdateLabel;
    private Label temperatureLabel, feelsLikeLabel, humidityLabel, pressureLabel, windSpeedLabel;
    private Label visibilityLabel, sunriseLabel, sunsetLabel, dateLabel;
    private Label weatherAlertsLabel;
    private String weatherDataJson;


    /**
     * Initializes and displays the primary stage (window) of the JavaFX application.
     * This method sets up the main user interface for the weather dashboard.
     * It arranges UI components within a {@code GridPane}, sets the scene, and shows the primary stage.
     * Additionally, it calls {@code updateWeatherData} to load the initial weather data.
     *
     * @param primaryStage The primary stage for this application, onto which the scene is set.
     *                     This stage is created by the JavaFX platform.
     * @override Indicates that this method overrides a method declared in a superclass.
     */

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Wetter-Dashboard");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        initializeUIComponents(grid);
        initializeManualEntryComponents(grid);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateWeatherData(null, null); // Lädt die Anfangswerte

    }

    /**
     * Initializes and adds UI components to the given GridPane for the weather dashboard.
     * This method sets up various UI elements such as a ComboBox for city selection,
     * a refresh button, labels for displaying weather information, and a button to display the temperature graph.
     * It also configures action handlers for interactive components to trigger relevant actions.
     *
     * @param grid The GridPane to which the UI components are added. This should be part of the primary scene layout.
     */

    private void initializeUIComponents(GridPane grid) {
        citySelection = new ComboBox<>();
        citySelection.getItems().addAll("Salzburg", "Wien", "Graz", "Innsbruck", "Linz");
        citySelection.setValue("Salzburg");
        citySelection.setOnAction(event -> updateWeatherData(null, null));

        refreshButton = new Button("Aktualisieren");
        refreshButton.setOnAction(event -> updateWeatherData(null, null));


        lastUpdateLabel = new Label("Letzte Aktualisierung: Nicht verfügbar");
        grid.add(lastUpdateLabel, 3, 15, 2, 1);


        weatherAlertsLabel = new Label("Wetterwarnungen: Keine");
        grid.add(weatherAlertsLabel, 2, 5, 2, 1);

        graphButton = new Button("Zeige Graph");
        graphButton.setOnAction(event -> showGraphWindow());
        grid.add(graphButton, 0, 10);


        temperatureLabel = new Label("Temperatur: ");
        feelsLikeLabel = new Label("Fühlt sich an wie: ");
        humidityLabel = new Label("Luftfeuchtigkeit: ");
        pressureLabel = new Label("Luftdruck: ");
        windSpeedLabel = new Label("Windgeschwindigkeit: ");
        visibilityLabel = new Label("Sicht: ");
        sunriseLabel = new Label("Sonnenaufgang: ");
        sunsetLabel = new Label("Sonnenuntergang: ");
        dateLabel = new Label("Datum: ");

        grid.add(citySelection, 0, 0);
        grid.add(refreshButton, 1, 0);
        grid.add(temperatureLabel, 0, 1);
        grid.add(feelsLikeLabel, 0, 2);
        grid.add(humidityLabel, 0, 3);
        grid.add(pressureLabel, 0, 4);
        grid.add(windSpeedLabel, 0, 5);
        grid.add(visibilityLabel, 0, 6);
        grid.add(sunriseLabel, 0, 7);
        grid.add(sunsetLabel, 0, 8);
        grid.add(dateLabel, 0, 9);
    }

    /**
     * Initializes components for manual entry of latitude and longitude coordinates in the weather dashboard.
     * This method sets up text fields for latitude and longitude input, a confirm button to submit the entered coordinates,
     * and a label for displaying any errors related to manual entry.
     * It also configures the action handler for the confirm button to process the manually entered data.
     *
     * @param grid The GridPane to which the manual entry components are added. This grid is part of the primary scene layout
     *             and should be configured to accommodate these components.
     */
    private void initializeManualEntryComponents(GridPane grid) {
        latitudeInput = new TextField();
        longitudeInput = new TextField();
        confirmButton = new Button("Bestätigen");
        manualEntryErrorLabel = new Label();

        latitudeInput.setPromptText("Breitengrad");
        longitudeInput.setPromptText("Längengrad");
        confirmButton.setOnAction(event -> handleManualEntry());

        grid.add(new Label("Breitengrad:"), 2, 0);
        grid.add(latitudeInput, 3, 0);
        grid.add(new Label("Längengrad:"), 2, 1);
        grid.add(longitudeInput, 3, 1);
        grid.add(confirmButton, 3, 2);
        grid.add(manualEntryErrorLabel, 2, 3, 2, 1);
    }

    /**
     * Handles the manual entry of latitude and longitude coordinates.
     * This method parses the latitude and longitude values entered by the user, validates them,
     * and updates the weather data accordingly. If the coordinates are outside valid ranges
     * (latitude: -90 to 90, longitude: -180 to 180), an error message is displayed.
     * In case of invalid input format, an error message is also shown to the user.
     *
     * This method is typically invoked as an action handler for a button or similar UI component.
     */

    private void handleManualEntry() {
        try {
            double latitude = Double.parseDouble(latitudeInput.getText());
            double longitude = Double.parseDouble(longitudeInput.getText());

            if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("Ungültige Koordinaten");
            }

            updateWeatherData(latitude, longitude);
            manualEntryErrorLabel.setText("");
        } catch (IllegalArgumentException e) {
            manualEntryErrorLabel.setText("Ungültige Eingabe. Bitte gültige Koordinaten eingeben.");
        }
    }

    /**
     * Updates the weather data by either using provided latitude and longitude or selecting default coordinates
     * based on the selected city from the ComboBox. This method creates and starts a background task
     * to fetch weather data asynchronously, ensuring the UI remains responsive.
     *
     * The method uses default coordinates for predefined cities if latitude and longitude are not provided.
     * It then makes a call to a weather API and updates the UI with the retrieved data. If the API call fails,
     * it sets the UI labels to indicate unavailable data.
     *
     * This method should be called whenever there is a need to update the weather information displayed in the UI,
     * such as after selecting a city or entering new coordinates.
     *
     * @param latitude  The latitude coordinate for which weather data is to be fetched. If null, the method
     *                  uses the latitude of the selected city from the ComboBox.
     * @param longitude The longitude coordinate for which weather data is to be fetched. If null, the method
     *                  uses the longitude of the selected city from the ComboBox.
     */
    private void updateWeatherData(Double latitude, Double longitude) {
        Task<Void> weatherTask = new Task<Void>() {
            @Override
            protected Void call() {
                Double finalLatitude = latitude;
                Double finalLongitude = longitude;

                if (finalLatitude == null || finalLongitude == null) {
                    String selectedCity = citySelection.getValue();
                    switch (selectedCity) {
                        case "Wien":
                            finalLatitude = 48.2082;
                            finalLongitude = 16.3738;
                            break;
                        case "Graz":
                            finalLatitude = 47.0707;
                            finalLongitude = 15.4395;
                            break;
                        case "Innsbruck":
                            finalLatitude = 47.2692;
                            finalLongitude = 11.4041;
                            break;
                        case "Linz":
                            finalLatitude = 48.3069;
                            finalLongitude = 14.2858;
                            break;
                        default:
                            finalLatitude = 47.8112; // Salzburg
                            finalLongitude = 13.0332;
                            break;
                    }
                }

                WeatherAPI weatherAPI = new WeatherAPI();
                String weatherData = weatherAPI.getWeatherData(finalLatitude, finalLongitude);

                if (weatherData != null) {

                    weatherDataJson = weatherData;
                    JSONObject json = new JSONObject(weatherData);
                    JSONObject current = json.getJSONObject("current");

                    double tempKelvin = current.getDouble("temp");
                    double feelsLikeKelvin = current.getDouble("feels_like");
                    double tempCelsius = kelvinToCelsius(tempKelvin);
                    double feelsLikeCelsius = kelvinToCelsius(feelsLikeKelvin);
                    int humidity = current.getInt("humidity");
                    int pressure = current.getInt("pressure");
                    double windSpeedMps = current.getDouble("wind_speed");
                    double windSpeedKmh = mpsToKmh(windSpeedMps);
                    int visibility = current.getInt("visibility");
                    long sunriseTime = current.getLong("sunrise");
                    long sunsetTime = current.getLong("sunset");
                    String sunrise = formatUnixTime(sunriseTime);
                    String sunset = formatUnixTime(sunsetTime);
                    String currentDate = formatDate(Instant.now().getEpochSecond());

                    Platform.runLater(() -> {
                        lastUpdateLabel.setText("Letzte Aktualisierung: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                        temperatureLabel.setText(String.format("Temperatur: %.2f °C", tempCelsius));
                        feelsLikeLabel.setText(String.format("Fühlt sich an wie: %.2f °C", feelsLikeCelsius));
                        humidityLabel.setText("Luftfeuchtigkeit: " + humidity + "%");
                        pressureLabel.setText("Luftdruck: " + pressure + " hPa");
                        windSpeedLabel.setText(String.format("Windgeschwindigkeit: %.2f km/h", windSpeedKmh));
                        visibilityLabel.setText("Sicht: " + visibility + " m");
                        sunriseLabel.setText("Sonnenaufgang: " + sunrise);
                        sunsetLabel.setText("Sonnenuntergang: " + sunset);
                        dateLabel.setText("Datum: " + currentDate);

                        if (json.has("alerts")) {
                            JSONArray alerts = json.getJSONArray("alerts");
                            StringBuilder alertsText = new StringBuilder("Wetterwarnungen:\n");
                            for (int i = 0; i < alerts.length(); i++) {
                                JSONObject alert = alerts.getJSONObject(i);
                                String event = alert.getString("event");
                                String description = alert.getString("description");
                                alertsText.append(event).append("\n").append(description).append("\n\n");
                            }
                            weatherAlertsLabel.setText(alertsText.toString());
                        } else {
                            weatherAlertsLabel.setText("Wetterwarnungen: Keine");
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        setLabelsToUnavailable();
                        lastUpdateLabel.setText("Letzte Aktualisierung: Fehlgeschlagen");
                    });
                }
                return null;
            }
        };

        new Thread(weatherTask).start();
    }

    /**
     * Displays a window with a line chart representing temperature data.
     * This method creates a new stage (window) and displays the line chart generated
     * by the {@code createTemperatureGraph} method within it.
     * The window size is set to 600x400 pixels by default.
     *
     * It retrieves the temperature data from the instance variable {@code weatherDataJson},
     * which is expected to be a JSON string containing temperature data.
     */

    private void showGraphWindow() {
        Stage graphStage = new Stage();
        graphStage.setTitle("Temperatur Graph");

        LineChart<String, Number> temperatureGraph = createTemperatureGraph(weatherDataJson);

        Scene graphScene = new Scene(temperatureGraph, 600, 400);

        graphStage.setScene(graphScene);
        graphStage.show();
    }


    /**
     * Creates a line chart for temperature data based on the provided JSON data.
     * This method parses the given JSON string to extract temperature data and uses it to
     * create a line chart. The chart displays high and low temperatures over time.
     *
     * @param jsonData The JSON string containing the temperature data. This should be
     *                 a properly formatted JSON, containing keys like 'daily', 'dt', and 'temp'.
     * @return A {@code LineChart<String, Number>} object representing the temperature graph.
     * @throws org.json.JSONException if the provided JSON string is not properly formatted or
     *         if expected data is missing in the JSON object.
     */

    private LineChart<String, Number> createTemperatureGraph(String jsonData) {

        // Create axis for the diagram
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Datum");
        yAxis.setLabel("Temperatur (°C)");

        // Create linechart
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Temperaturverlauf");

        // Create rows for high and low temperatures
        XYChart.Series<String, Number> highSeries = new XYChart.Series<>();
        highSeries.setName("Höchsttemperatur");
        XYChart.Series<String, Number> lowSeries = new XYChart.Series<>();
        lowSeries.setName("Tiefsttemperatur");

        // Assigning the json data
        JSONObject json = new JSONObject(jsonData);
        JSONArray dailyArray = json.getJSONArray("daily");

        // assigning the data point to the diagram
        for (int i = 0; i < dailyArray.length(); i++) {
            JSONObject dayData = dailyArray.getJSONObject(i);

            // change date format
            long timeEpoch = dayData.getLong("dt");
            String dateString = Instant.ofEpochSecond(timeEpoch)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM"));

            double maxTemp = kelvinToCelsius(dayData.getJSONObject("temp").getDouble("max"));
            double minTemp = kelvinToCelsius(dayData.getJSONObject("temp").getDouble("min"));

            // datt data points
            highSeries.getData().add(new XYChart.Data<>(dateString, maxTemp));
            lowSeries.getData().add(new XYChart.Data<>(dateString, minTemp));
        }

        lineChart.getData().addAll(highSeries, lowSeries);

        return lineChart;
    }





    /**
     * Sets the text of all weather-related labels in the UI to indicate that data is unavailable.
     * This method is typically called when weather data cannot be fetched or is otherwise unavailable,
     * ensuring that the user interface accurately reflects the lack of data.
     *
     * It updates labels related to temperature, feels-like temperature, humidity, pressure, wind speed,
     * visibility, sunrise, sunset, and the date to show a message indicating data unavailability.
     */

    private void setLabelsToUnavailable() {
        temperatureLabel.setText("Temperatur: Nicht verfügbar");
        feelsLikeLabel.setText("Fühlt sich an wie: Nicht verfügbar");
        humidityLabel.setText("Luftfeuchtigkeit: Nicht verfügbar");
        pressureLabel.setText("Luftdruck: Nicht verfügbar");
        windSpeedLabel.setText("Windgeschwindigkeit: Nicht verfügbar");
        visibilityLabel.setText("Sicht: Nicht verfügbar");
        sunriseLabel.setText("Sonnenaufgang: Nicht verfügbar");
        sunsetLabel.setText("Sonnenuntergang: Nicht verfügbar");
        dateLabel.setText("Datum: Nicht verfügbar");
    }

    /**
     * Converts temperature from Kelvin to Celsius.
     *
     * @param kelvin The temperature in Kelvin.
     * @return The converted temperature in Celsius.
     */

    private double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    /**
     * Converts speed from meters per second to kilometers per hour.
     *
     * @param mps The speed in meters per second.
     * @return The speed in kilometers per hour.
     */

    private double mpsToKmh(double mps) {
        return mps * 3.6;
    }

    /**
     * Formats a Unix timestamp into a time string.
     *
     * @param unixTime The Unix timestamp.
     * @return The formatted time string.
     */

    private String formatUnixTime(long unixTime) {
        return Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * Formats a Unix timestamp into a date string.
     *
     * @param unixTime The Unix timestamp.
     * @return The formatted date string.
     */

    private String formatDate(long unixTime) {
        return Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command line arguments.
     */

    public static void main(String[] args) {
        launch(args);
    }
}


