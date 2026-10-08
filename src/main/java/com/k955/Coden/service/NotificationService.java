package com.k955.Coden.service;

import com.k955.Coden.dtos.Notification.NotificationResponse;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Notification.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {

    NotificationResponse getNotificationById(UUID notificationId);

    Page<NotificationResponse> getMyNotifications(boolean isRead, Pageable pageable);

    Integer getUnreadNotificationCount();

    NotificationResponse markNotificationAsRead(UUID notificationId);

    void readAllNotifications();

    void notifyAdmin(NotificationType notificationType, UUID referenceId, String message, UUID actorId);

    void notifySuperAdmins(NotificationType notificationType, UUID referenceId, String message, UUID actorId);

}
