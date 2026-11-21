package com.scalegate.scale_gate.RedisStream.Publisher;

import com.scalegate.scale_gate.Event.VehicleFlaggedEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class VehicleEventPublisher {

    private static final String STREAM_KEY = "scalegate:vehicle-events";

    private final StringRedisTemplate redisTemplate;

    public void publish(VehicleFlaggedEvent event) {

        Map<String, String> eventData = Map.of(
                "eventType", event.eventType(),
                "vehicleId", event.vehicleId().toString(),
                "eventTimestamp", event.eventTimestamp().toString(),
                "flaggedReason", event.flaggedReason()
        );

        MapRecord<String, String, String> record =
                StreamRecords.newRecord()
                        .in(STREAM_KEY)
                        .ofMap(eventData);

        redisTemplate.opsForStream().add(record);
    }
}
