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
}