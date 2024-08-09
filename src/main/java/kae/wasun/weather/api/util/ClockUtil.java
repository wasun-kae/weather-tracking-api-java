package kae.wasun.weather.api.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class ClockUtil {

    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneId.of("UTC"));

    public Instant getCurrentTime() {
        return Instant.now();
    }

    public String convertToString(Instant instant) {
        return formatter.format(instant);
    }
}
