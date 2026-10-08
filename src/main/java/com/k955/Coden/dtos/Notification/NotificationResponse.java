package com.k955.Coden.dtos.Notification;

import com.k955.Coden.enums.Notification.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType notificationType,
        String message,
        UUID referenceId,
        boolean isRead,
        Instant createdAt
) {
}
