package com.k955.Coden.controller;

import com.k955.Coden.dtos.Notification.NotificationResponse;
import com.k955.Coden.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(
            @RequestParam(required = false) boolean isRead,
            @PageableDefault(size = 5, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(notificationService.getMyNotifications(isRead, pageable));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadNotificationCount() {
        return ResponseEntity.ok(notificationService.getUnreadNotificationCount());
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAllNotifications() {
        notificationService.readAllNotifications();
        return ResponseEntity.noContent().build();
    }

}
