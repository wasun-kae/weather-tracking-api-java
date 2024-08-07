package kae.wasun.weather.api.model.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Device {

    private String id;
    private Instant createdAt;
}
