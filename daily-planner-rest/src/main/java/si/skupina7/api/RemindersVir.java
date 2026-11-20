package si.skupina7.api;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import si.skupina7.api.model.Reminder;
import si.skupina7.api.model.ReminderList;
import si.skupina7.api.model.ReminderPatchRequest;
import si.skupina7.api.model.ReminderRequest;


@ApplicationScoped
public class RemindersVir implements RemindersApi {
    

    // mock base {id, reminder}
    private final Map<String, Reminder> reminders = new ConcurrentHashMap<>();


/*   @Override
    public Dogodek createDogodek(@Valid @NotNull Dogodek dogodek) {
        System.out.println("Poklicana je bila metoda za dodajanje dogodka!");
        dogodek.setId(UUID.randomUUID().toString());
        dogodki.put(dogodek.getId(), dogodek);
        return dogodek;
    }

    Reminder:
      type: "object"
      required:
        - "id"
        - "content"
        - "dueAt"
        - "sentAt"
      properties:
        id:
          type: "string"
          description: "Unique reminder ID"
          example: "rem123"

        content:
          type: "string"
          example: "Class scheduled"

        dueAt:
          type: "string"
          format: "date-time"
          example: "2026-11-12T16:30:00+01:00" # YYYY-MM-DDTHH:MM:SS+zamik(HH:MM)

        sentAt:
          type: "string"
          format: "date-time"
          description: "Indicates when the push notification has been sent. If the parameter is emprty it indicates that the notification has yet to be sent."
          default: null
          example: "2026-05-12T16:30:00+01:00"
 */


/**
* Creates a reminder belonging to the user.
 *
 * @param reminderRequest 
 * @return Reminder created successfully
 * @return Invalid reminder data
 * @return User not found
 */
    @Override 
    public Reminder createReminder(ReminderRequest reminderRequest) {
        System.out.println("This method is called when creating a new reminder!");

        // err 400 invalid data: 
        if (reminderRequest == null|| reminderRequest.getContent() == null || reminderRequest.getDueAt() == null) {
            throw new BadRequestException("Invalid reminder data");
        }

        // 404 user not found:
        // because there is no user management implemented, we will assume that user always exists
        // TODO: when user management is implemented, add this check if the user exists in the database
        // if (!userExists(reminderRequest.getUserId())) {
        //     throw new NotFoundException("User not found");
        // }

        Reminder reminder = new Reminder();
        reminder.setId(UUID.randomUUID().toString());
        reminder.setContent(reminderRequest.getContent());
        reminder.setDueAt(reminderRequest.getDueAt());

        reminders.put(reminder.getId(), reminder);
        return reminder;

    }

     /**
     * Deletes a reminder using its unique id
     *
     * @param reminderId Unique ID of the reminder
     * @return Reminder deleted successfully
     * @return Reminder not found
     */
    @Override 
    public void deleteReminder(String reminderId) {
        System.out.println("This method is called when deleting a reminder!");
        // if (reminders.containsKey(reminderId)) {
        //     reminders.remove(reminderId);
        // } else {
        //     throw new NotFoundException("Reminder not found");
        // }

        // ATOMIC OPERATION:
        // reminders.remove(reminderId) is a single atomic operation 
        // on a ConcurrentHashMap and returns the removed value, 
        // or null if the key wasn’t there.
        if (reminders.remove(reminderId) == null) {
            throw new NotFoundException("Reminder not found");
        }
    }



     /**
     * Retrives a list of all the existing reminders that have to be sent out as push notifications
     *
     * @param dueBefore Retrives only the reminders that are due before the specifed date and time
     * @return Reminders retrived successfully
     */
    @Override
    public ReminderList getAllReminders(OffsetDateTime dueBefore) {
        System.out.println("This method is called when retrieving all reminders that are due before the specified date and time!");
        List<Reminder> filteredReminders = new ArrayList<>();

        for (Reminder r : reminders.values()){
            if(r.getSentAt() == null && (dueBefore == null || r.getDueAt().isBefore(dueBefore))){
                filteredReminders.add(r);
            }
        }
        filteredReminders.sort((a,b)-> a.getDueAt().compareTo(b.getDueAt()));

        /*
        ReminderList:
        type: object
        properties:
            reminders:
                type: array
                items:
                    $ref: '#/components/schemas/Reminder'
        */

        // Create a ReminderList object and set the filtered reminders
        ReminderList reminderList = new ReminderList();
        reminderList.setReminders(filteredReminders);
        return reminderList;
    }

    /**
     * Returns ALL reminders belonging to the user. Even the ones that have already been sent out as push notifications.!
     *
     * @return Person's reminders retrieved successfully
     * @return User not found
     */
    @Override
    public ReminderList getPersonReminders() {
        System.out.println("This method is called when retrieving ALL reminders belonging to the user!");
        
        // 404 user not found:
        // because there is no user management implemented, we will assume that user always exists
        // TODO: when user management is implemented, add this check if the user exists in the database
        // if (!userExists(reminderRequest.getUserId())) {
        //     throw new NotFoundException("User not found");
        // }
        
        List<Reminder> all = new ArrayList<>(reminders.values());
        all.sort((a,b)-> a.getDueAt().compareTo(b.getDueAt()));

        ReminderList list = new ReminderList();
        list.setReminders(all);
        return list;
    }

    /**
     * Updates the sentAt paramater in reminders. Is called after the push notification of the reminder with the reminderId has been sent
     *
     * @param reminderId Unique ID of the reminder
     * @return Reminder updated successfully
     * @return Reminder not found
     */
    @Override 
    public void patchReminders(String reminderId) {
        System.out.println("This method is called when updating the sentAt parameter of a reminder!");
        // if (!reminders.containsKey(reminderId)) {
        //     throw new NotFoundException("Reminder not found");
        // }

        // Reminder reminder = reminders.get(reminderId);
        // // Update the sentAt parameter
        // reminder.setSentAt(OffsetDateTime.now()); // Set the current date and time as sentAt
        // reminders.put(reminderId, reminder);

        // ATOMIC OPERATION:
        // computeIfPresent runs atomically per key, so concurrent updates can't interleave!!
        // computeIfPresent will only update the reminder if it is present in the map, otherwise it will return null
        //example of use:     capitalCities.computeIfPresent("England", (k, v) -> v + "(" + k + ")");
        Reminder updatedReminder = reminders.computeIfPresent(reminderId, (id, reminder) -> {
            reminder.setSentAt(OffsetDateTime.now());
            return reminder;
        });
        if (updatedReminder == null) {
            throw new NotFoundException("Reminder not found");
        }
    }

    /**
     * Updates the reminder using its unique id. Only the content and dueAt parameters can be updated.
     *
     * @param reminderId Unique ID of the reminder
     * @param reminderPatchRequest 
     * @return Reminder updated successfully
     * @return Invalid reminder data
     * @return Reminder not found
     */
    @Override
    public Reminder patchRemindersUser(String reminderId, ReminderPatchRequest reminderPatchRequest) {
        // Reminder reminder = reminders.get(reminderId);
        // if (reminder == null) {
        //     throw new NotFoundException("Reminder not found");
        // }
        // if (reminderPatchRequest.getContent() != null) {
        //     reminder.setContent(reminderPatchRequest.getContent());
        // }
        // if (reminderPatchRequest.getDueAt() != null) {
        //     reminder.setDueAt(reminderPatchRequest.getDueAt());
        //     reminder.setSentAt(null);                       // new reminder has been set, so the sentAt parameter should be reset to null, indicating that the push notification has not been sent yet
        // }
        // return reminder;

        if (reminderPatchRequest == null) {
                    throw new BadRequestException("Invalid reminder data");
        }

        // ATOMIC OPERATION:
        Reminder updatedReminder = reminders.computeIfPresent(reminderId, (id, reminder) -> {
            if (reminderPatchRequest.getContent() != null) {
                reminder.setContent(reminderPatchRequest.getContent());
            }

            OffsetDateTime newDueAt = reminderPatchRequest.getDueAt();
            if (newDueAt != null) {    
                if (!newDueAt.isEqual(reminder.getDueAt())){    // isEqual compares the instant (ignores offset differences) for example: 2026-11-12T16:30:00+01:00 isEqual to 2026-11-12T15:30:00Z
                    reminder.setDueAt(reminderPatchRequest.getDueAt());                    
                    reminder.setSentAt(null);                       // new reminder has been set, so the sentAt parameter should be reset to null, indicating that the push notification has not been sent yet
                }
            }
            return reminder;
        });
        if (updatedReminder == null) {
            throw new NotFoundException("Reminder not found");
        }
        return updatedReminder;
    }

}
