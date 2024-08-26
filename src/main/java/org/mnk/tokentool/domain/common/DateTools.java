package org.mnk.tokentool.domain.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateTools {

    public static final String BEFORE = "BEFORE";
    public static final String AFTER = "AFTER";
    public static final String SAME = "SAME";

    public static Date localDateTimeToDate(LocalDateTime dateToConvert) {

        return Objects.isNull(dateToConvert) ?
                null :
                Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return Objects.isNull(date) ?
                null :
                date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
    }

    public static boolean isSameDate(final Date date) {

        return SAME.equals(getDateStatus(date));
    }

    public static boolean isAfterToday(final Date date) {

        return AFTER.equals(getDateStatus(date));
    }

    public static boolean isBeforeToday(final Date date) {

        return BEFORE.equals(getDateStatus(date));
    }

    public static String getDateStatus(final Date givenDate) {
        Date currentDate = new Date();

        Calendar calendarGiven = Calendar.getInstance();
        calendarGiven.setTime(givenDate);

        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTime(currentDate);

        if (calendarGiven.after(calendarCurrent)) {
            return AFTER;
        }

        if (calendarGiven.before(calendarCurrent)) {
            return BEFORE;
        }
        return SAME;
    }
}
