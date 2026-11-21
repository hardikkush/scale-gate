package com.scalegate.scale_gate.Event;

import java.time.OffsetDateTime;

public record VehicleFlaggedEvent(
        String eventType,
        Long vehicleId,
        OffsetDateTime eventTimestamp,
        String flaggedReason
) {
}