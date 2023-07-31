import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApp {

    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=" + API_KEY;

    private static JSONObject getWeatherData() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONObject(response.toString());
    }

    private static void printHourlyWeather(JSONObject data) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            String time = entry.getString("dt_txt");
            String weatherDescription = entry.getJSONArray("weather").getJSONObject(0).getString("description");
            System.out.println("Time: " + time + ", Weather: " + weatherDescription);
        }
    }

    private static void printWindSpeed(JSONObject data) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            String time = entry.getString("dt_txt");
            double windSpeed = entry.getJSONObject("wind").getDouble("speed");
            System.out.println("Time: " + time + ", Wind Speed: " + windSpeed + " m/s");
        }
    }

    private static void printPressure(JSONObject data) {
        JSONArray list = data.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject entry = list.getJSONObject(i);
            String time = entry.getString("dt_txt");
            double pressure = entry.getJSONObject("main").getDouble("pressure");
            System.out.println("Time: " + time + ", Pressure: " + pressure + " hPa");
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Get weather");
                System.out.println("2. Get Wind Speed");
                System.out.println("3. Get Pressure");
                System.out.println("0. Exit");

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String option = br.readLine();

                if (option.equals("1")) {
                    JSONObject weatherData = getWeatherData();
                    printHourlyWeather(weatherData);
                } else if (option.equals("2")) {
                    JSONObject weatherData = getWeatherData();
                    printWindSpeed(weatherData);
                } else if (option.equals("3")) {
                    JSONObject weatherData = getWeatherData();
                    printPressure(weatherData);
                } else if (option.equals("0")) {
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while making API request: " + e.getMessage());
        }
    }
}
