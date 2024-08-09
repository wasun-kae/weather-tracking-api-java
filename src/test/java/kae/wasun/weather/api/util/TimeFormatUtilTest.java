package kae.wasun.weather.api.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeFormatUtilTest {

    @Test
    void should_return_correct_utc_timestamp_format() {
        var instant = Instant.parse("2024-08-07T12:34:56.789123Z");
        var actual = TimeFormatUtil.convertToString(instant);

        assertThat(actual).isEqualTo("2024-08-07T12:34:56.789Z");
    }
}