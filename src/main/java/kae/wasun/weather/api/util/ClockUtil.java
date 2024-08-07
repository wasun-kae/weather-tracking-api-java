package kae.wasun.weather.api.util;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ClockUtil {

    public Instant getCurrentTime() {
        return Instant.now();
    }
}
