/*
 * beetlejuice
 * beetlejuice-contact
 * Copyright (C) 2011-2013 art of coding UG, http://www.art-of-coding.eu
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 12.04.13 15:27
 */

package eu.artofcoding.beetlejuice.contact;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.Person;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.CuType;
import net.fortuna.ical4j.model.parameter.PartStat;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 
 * @author otto
 */
public final class GoogelCalendarToIcs {

    private static CalendarService googleService;

    private static Uid uid;

    private static CalendarService connect() throws AuthenticationException {
        // Create a new Calendar service
        if (null == googleService) {
            googleService = new CalendarService("My Application");
            googleService.setUserCredentials("ralfbensmann", "xxx");
        }
        return googleService;
    }

    private static CalendarEventFeed getCalendarFeed(String calendarURL) throws AuthenticationException, MalformedURLException, IOException, ServiceException {
        // Set up the URL and the object that will handle the connection:
        // "http://www.google.com/calendar/feeds/default/private/full"
        URL feedUrl = new URL(calendarURL);
        // Send the request and receive the response:
        return connect().getFeed(feedUrl, CalendarEventFeed.class);

    }

    private static Calendar createCalendar(String organizer, TimeZone timezone) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//" + organizer + "//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        calendar.getProperties().add(new XProperty("X-WR-CALNAME", organizer));
        calendar.getProperties().add(new XProperty("X-WR-TIMEZONE", timezone.getDisplayName()));
        return calendar;
    }

    private static VEvent createEvent(CalendarEventEntry entry, TimeZone timezone) {
        // VTimeZone tz = timezone.getVTimeZone();
        // java.util.Calendar cal = java.util.Calendar.getInstance(timezone);
        // //
        // cal.set(java.util.Calendar.YEAR, 2005);
        // cal.set(java.util.Calendar.MONTH, java.util.Calendar.NOVEMBER);
        // cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        // cal.set(java.util.Calendar.HOUR_OF_DAY, 15);
        // cal.clear(java.util.Calendar.MINUTE);
        // cal.clear(java.util.Calendar.SECOND);
        // //
        // DateTime dt = new DateTime(cal.getTime());
        // dt.setTimeZone(timezone);
        java.util.Calendar startDate = new GregorianCalendar();
        java.util.Calendar endDate = new GregorianCalendar();
        try {
            When when = entry.getTimes().get(0);
            // Start date
            startDate.setTimeZone(timezone);
            startDate.setTimeInMillis(when.getStartTime().getValue());
            // startDate.set(java.util.Calendar.MONTH, );
            // startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
            // startDate.set(java.util.Calendar.YEAR, 2008);
            // startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
            // startDate.set(java.util.Calendar.MINUTE, 0);
            // startDate.set(java.util.Calendar.SECOND, 0);
            // End date
            endDate.setTimeZone(timezone);
            endDate.setTimeInMillis(when.getEndTime().getValue());
            // endDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
            // endDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
            // endDate.set(java.util.Calendar.YEAR, 2008);
            // endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
            // endDate.set(java.util.Calendar.MINUTE, 0);
            // endDate.set(java.util.Calendar.SECOND, 0);
            //
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        VEvent event = new VEvent(new DateTime(startDate.getTime()), new DateTime(endDate.getTime()), entry.getTitle().getPlainText());
        event.getProperties().add(uid);
        //
        Person p = entry.getAuthors().get(0);
        try {
            Organizer organizer = new Organizer();
            organizer.setValue("mailto:" + p.getEmail());
            organizer.getParameters().add(new Cn(p.getName()));
            event.getProperties().add(organizer);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //
        for (EventWho w : entry.getParticipants()) {
            try {
                Attendee att = new Attendee();
                att.setValue("mailto:" + w.getEmail());
                att.getParameters().add(new Role("REQ-PARTICIPANT"));
                att.getParameters().add(new CuType(w.getAttendeeType()));
                String status = null;
                try {
                    if (w.getAttendeeStatus().indexOf("invited") > 0) {
                        status = "NEEDS-ACTION";
                    } else if (w.getAttendeeStatus().indexOf("accepted") > 0) {
                        status = "ACCEPTED";
                    } else if (w.getAttendeeStatus().indexOf("declined") > 0) {
                        status = "DECLINED";
                    } else if (w.getAttendeeStatus().indexOf("tentative") > 0) {
                        status = "TENTATIVE";
                    }
                } catch (NullPointerException e) {
                }
                att.getParameters().add(new PartStat(status));
                att.getParameters().add(new Cn(w.getValueString()));
                event.getProperties().add(att);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        //
        return event;
    }

    public static void main(String[] args) {
        FileOutputStream fout = null;
        //
        try {
            uid = new UidGenerator("uidGen").generateUid();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //
        TimeZoneRegistry timeZoneRegistry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timeZone = timeZoneRegistry.getTimeZone("Europe/Berlin");
        // Get a list of all calendar entries
        URL metafeedUrl = null;
        CalendarFeed resultFeed = null;
        try {
            metafeedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
            resultFeed = connect().getFeed(metafeedUrl, CalendarFeed.class);
            // Walk through every calendar
            CalendarEntry entry = null;
            Calendar calendar = null;
            List<CalendarEntry> entries = resultFeed.getEntries();
            for (int i = 0; i < entries.size(); i++) {
                entry = entries.get(i);
                String calendarName = entry.getTitle().getPlainText();
                // HACK rebuild URL
                String[] ar = entry.getId().split("/");
                String f = ar[0] + "/" + ar[1] + "/" + ar[2] + "/" + ar[3] + "/" + ar[4] + "/" + ar[8] + "/private/full";
                System.out.println("\t" + calendarName + " -> " + entry.getId() + " ->" + f);
                //
                calendar = createCalendar(calendarName, timeZone);
                //
                for (CalendarEventEntry calendarEntry : getCalendarFeed(f).getEntries()) {
                    calendar.getComponents().add(createEvent(calendarEntry, timeZone));
                }
                // Write ics file
                try {
                    fout = new FileOutputStream(calendarName + ".ics");
                    CalendarOutputter outputter = new CalendarOutputter();
                    outputter.output(calendar, fout);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    e.printStackTrace();
                } finally {
                    if (null != fout) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
