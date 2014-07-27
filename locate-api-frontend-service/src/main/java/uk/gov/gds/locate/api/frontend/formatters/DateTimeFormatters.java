package uk.gov.gds.locate.api.frontend.formatters;

import java.text.DateFormat;

public abstract class DateTimeFormatters {
//    public final static String internalDateFormatRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
//    public final static DateTimeFormatter internalDateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withLocale(Locale.UK);
//
//    public final static DateTimeFormatter internalDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.UK);

    public final static DateFormat internalDateFormatter = DateFormat.getDateInstance();

}

