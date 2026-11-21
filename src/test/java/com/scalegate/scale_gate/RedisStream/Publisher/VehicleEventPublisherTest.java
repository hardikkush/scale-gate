package com.scalegate.scale_gate.RedisStream.Publisher;

import com.scalegate.scale_gate.Event.VehicleFlaggedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;

@SpringBootTest
class VehicleEventPublisherTest {

    @Autowired
    private VehicleEventPublisher vehicleEventPublisher;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void shouldPublishVehicleFlaggedEventToRedisStream() {

        VehicleFlaggedEvent event = new VehicleFlaggedEvent(
                "VEHICLE_REGISTERED_AS_FLAGGED",
                105L,
                OffsetDateTime.now(),
                "MOCK_FLAGGED_VEHICLE"
        );

        vehicleEventPublisher.publish(event);

        List<MapRecord<String, Object, Object>> records =
                redisTemplate.opsForStream()
                        .range("scalegate:vehicle-events", Range.unbounded());

        Map<Object, Object> matchingEvent = records.stream()
                .map(MapRecord::getValue)
                .filter(data ->
                        "105".equals(data.get("vehicleId")) &&
                                "VEHICLE_REGISTERED_AS_FLAGGED".equals(data.get("eventType")) &&
                                "MOCK_FLAGGED_VEHICLE".equals(data.get("flaggedReason"))
                )
                .findFirst()
                .orElse(null);

        assertThat(matchingEvent).isNotNull();
    }
}
