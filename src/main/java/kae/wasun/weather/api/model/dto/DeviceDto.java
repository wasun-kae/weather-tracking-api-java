package kae.wasun.weather.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class DeviceDto {

    private String id;
    private Instant createdAt;
}
