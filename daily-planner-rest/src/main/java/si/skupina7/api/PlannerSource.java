package si.skupina7.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import si.skupina7.api.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlannerSource implements PlannerApi {

    @Inject
    WeatherApi weatherApi;

    //mock data
    private final String city = "Ljubljana";
    private final List<Reminder> remindersList = new ArrayList<>();
    private final List<ScheduleEvent> events = new ArrayList<>();
    private final List<CalendarDay> calendarDays = new ArrayList<>();

    /**
     * Returns the person's daily schedule and calendar, top 3 reminder and weather forecast.
     *
     * @param date The date for which events should be retrieved. Defaults to today.
     * @return Daily overview retrieved successfully
     * @return User not found
     */

    @Override
    public DailyOverview getDailyOverview(LocalDate date){

        //TODO: 404 user not found

        DailyOverview dailyOverview = new DailyOverview();

        if(date == null){
            date = LocalDate.now();
        }


        //FILTERING and SORTING will be done in the database query!
        /*
        events.removeIf(event ->
                !event.getEndTime().isAfter(OffsetDateTime.now())
        );
        events.sort(Comparator.comparing(ScheduleEvent::getStartTime));
        */

        dailyOverview.setDate(date);
        dailyOverview.setWeather(weatherApi.getCurrentWeather(city));
        dailyOverview.setTopReminders(remindersList.stream().limit(3).toList());
        dailyOverview.setSchedule(events);

        return dailyOverview;
    }


    /**
     * Returns the user's calendar for a specified month.
     *
     * @param month Month to retrieve in MM format
     * @param year Year to retrieve in YYYY format
     * @return Monthly calendar retrieved successfully
     * @return User not found
     */

    @Override
    public MonthlyCalendar getMonthlyCalendar(Integer month, Integer year){

        //TODO: 404 user not found
        //TODO: technically the month and year parameters are in path so they are required and shouldn't have a default but i believe it makes sense that
        // they do have them. This will be corrected after a talk with asistent.

        LocalDate today = LocalDate.now();

        if(year == null){
            year = today.getYear();
        }

        if(month == null){
            month = today.getMonthValue();
        }

        if(month < 1 || month > 12){
            throw new BadRequestException("The month parameter must be between 01 and 12.");
        }

        if(year < 1000 || year > 9999){
            throw new BadRequestException("The year parameter must be between 1000 and 9999.");
        }


        MonthlyCalendar monthlyCalendar = new MonthlyCalendar();
        //Sorting will be done by database query!

        monthlyCalendar.setMonth(month);
        monthlyCalendar.setYear(year);
        monthlyCalendar.setDays(calendarDays);

        return monthlyCalendar;
    }


}