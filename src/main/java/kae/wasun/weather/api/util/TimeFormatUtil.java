package kae.wasun.weather.api.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeFormatUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"));

    private TimeFormatUtil() {

    }

    public static String convertToString(Instant instant) {
        return DATE_TIME_FORMATTER.format(instant);
    }
}
