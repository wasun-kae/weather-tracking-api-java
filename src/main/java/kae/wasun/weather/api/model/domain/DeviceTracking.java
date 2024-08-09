package kae.wasun.weather.api.model.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class DeviceTracking {

    private Instant timestamp;
    private BigDecimal temperature;
    private BigDecimal humidity;
    
    private Instant createdAt;
}
