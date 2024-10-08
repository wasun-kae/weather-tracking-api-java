package kae.wasun.weather.api.model.document;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.math.BigDecimal;

@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class WeatherTrackingDocument {

    public static final String TABLE_NAME = "WeatherTracking";
    public static final BeanTableSchema<WeatherTrackingDocument> TABLE_SCHEMA =
            TableSchema.fromBean(WeatherTrackingDocument.class);

    @Getter(onMethod_ = {@DynamoDbPartitionKey})
    private String PK;

    @Getter(onMethod_ = {@DynamoDbSortKey})
    private String SK;

    private String id;
    private String timestamp;
    private BigDecimal temperature;
    private BigDecimal humidity;

    private String createdAt;
}
