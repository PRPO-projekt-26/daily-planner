# daily-planner

Daily planner for students: schedule, weather, events and reminders.

## About the project

Daily Planner is an app that helps students organize their day. It keeps calendar events, personal reminders and the weather forecast in one place.

It gives the user a quick overview of the day so they can prepare for it more easily.

## Features

### Daily overview
- Morning summary of the schedule, calendar events, reminders and weather
- Shows the 3 earliest upcoming reminders

### Weather
- Weather forecast for the current day

### Calendar and schedule
- Import and delete external calendars
- Import the schedule

### Reminders
- Create personal reminders
- Set the date and time of a reminder
- Display reminders together with the daily schedule
- Display reminders at set time 

## Members and service ownership

| Member | GitHub | Ownership |
|---|---|---|
| Medeja Geršak | [@MedejaGersak](https://github.com/MedejaGersak) | Not yet assigned |
| Tisa Biček | [@vamp3k](https://github.com/vamp3k) | Not yet assigned |

## Requirements

- Java 25 or newer
- Maven 3.9 or newer

## Build and run

Build the project:

```bash
mvn clean package
```

Run the REST service in development mode:

```bash
mvn -pl daily-planner-rest quarkus:dev
```

## Project status

The project is currently in the planning and design phase. Features and technical decisions may change during development.
