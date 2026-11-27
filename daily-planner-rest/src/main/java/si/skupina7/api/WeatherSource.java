package si.skupina7.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import si.skupina7.api.model.WeatherResponse;

@ApplicationScoped
public class WeatherSource implements WeatherApi{


    /**
     * Retrieve the current weather information for a specified location.
     *
     * @param city Name of the city to retrieve weather information for.
     * @return Successful retrieval of weather information.
     * @return Bad request. The city parameter is missing or invalid.
     * @return City not found
     */
    @Override
    public WeatherResponse getCurrentWeather(String city){

        //400 Bad request
        if(city == null || city.isBlank()){
            throw new BadRequestException("The city parameter cannot be empty.");
        }

        //TODO implement response 404 City not found

        WeatherResponse newWeatherResponse = new WeatherResponse();

        //mock data
        newWeatherResponse.setCity(city);
        newWeatherResponse.setCurrentTemperature(20.4);
        newWeatherResponse.setMaximumTemperature(23.6);
        newWeatherResponse.setMinimumTemperature(12.4);
        newWeatherResponse.setCloudCover("Partly cloudy");
        newWeatherResponse.setPrecipitation("There will be rain in the afternoon");
        newWeatherResponse.description("Partly cloudy with a storm in the afternoon");

        return newWeatherResponse;

    }
}
