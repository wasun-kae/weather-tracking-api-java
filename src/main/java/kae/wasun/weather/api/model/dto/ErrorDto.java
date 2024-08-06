package kae.wasun.weather.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    private String message;
}
