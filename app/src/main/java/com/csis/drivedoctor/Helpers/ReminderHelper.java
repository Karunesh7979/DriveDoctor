package com.csis.drivedoctor.Helpers;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

public class ReminderHelper {

        public static void setReminder(Context context, String title, String description, long startTime) {

            // Create a new calendar event intent
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);

            // Set the event details
            intent.putExtra(CalendarContract.Events.TITLE, title);
            intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + (60 * 60 * 1000)); // End time is 1 hour after start time

            // Set a reminder notification for the event
            intent.putExtra(CalendarContract.Events.HAS_ALARM, true);
            intent.putExtra(CalendarContract.Reminders.EVENT_ID, 0);
            intent.putExtra(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            intent.putExtra(CalendarContract.Reminders.MINUTES, 10); // Reminder notification will appear 10 minutes before the event

            // Launch the calendar event intent
            context.startActivity(intent);
        }



}
