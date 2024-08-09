package kae.wasun.weather.api.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class ClockUtilTest {

    private ClockUtil clockUtil;

    @BeforeEach
    void setUp() {
        clockUtil = new ClockUtil();
    }

    @Test
    void should_generate_current_date_time() {
        var fiveSecondsBeforeNow = Instant.now().minus(5, ChronoUnit.SECONDS);
        var fiveSecondsAfterNow = Instant.now().plus(5, ChronoUnit.SECONDS);

        var actual = clockUtil.getCurrentTime();

        assertThat(actual.isAfter(fiveSecondsBeforeNow)).isTrue();
        assertThat(actual.isBefore(fiveSecondsAfterNow)).isTrue();
    }

    @Test
    void should_return_correct_utc_timestamp_format() {
        var instant = Instant.parse("2024-08-07T12:34:56.789000Z");
        var actual = clockUtil.convertToString(instant);

        assertThat(actual).isEqualTo("2024-08-07T12:34:56.789Z");
    }
}