package com.k955.Coden.service.impl;

import com.k955.Coden.dtos.Notification.NotificationResponse;
import com.k955.Coden.entity.Notification;
import com.k955.Coden.entity.User;
import com.k955.Coden.enums.Notification.NotificationType;
import com.k955.Coden.enums.User.Role;
import com.k955.Coden.exception.AccessDeniedException;
import com.k955.Coden.exception.ResourceNotFoundException;
import com.k955.Coden.mapper.NotificationMapper;
import com.k955.Coden.repository.NotificationRepository;
import com.k955.Coden.repository.UserRepository;
import com.k955.Coden.security.JwtAuthUtil;
import com.k955.Coden.service.NotificationService;
import com.k955.Coden.specification.NotificationSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final JwtAuthUtil jwtAuthUtil;

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(UUID notificationId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(notificationId.toString(), "Notification"));

        if(!notification.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your notifications");
        }

        return notificationMapper.toNotificationResponse(notification);
    }

    @Override //TODO: Security Check
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getMyNotifications(boolean isRead, Pageable pageable) {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        return notificationRepository
                .findAll(NotificationSpecification.filterBy(isRead, userId), pageable)
                .map(notificationMapper::toNotificationResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getUnreadNotificationCount() {
        UUID userId = jwtAuthUtil.getCurrentUserId();
        int unreadCount = 0;

        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for(Notification notification : notifications) {
            if(!notification.isRead()) {
                unreadCount += 1;
            }
        }

        return unreadCount;
    }

    @Override
    @Transactional
    public NotificationResponse markNotificationAsRead(UUID notificationId) {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(notificationId.toString(), "Notification"));

        if(!notification.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only access your notifications");
        }

        notification.setRead(true);
        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toNotificationResponse(saved);
    }

    @Override
    @Transactional
    public void readAllNotifications() {
        UUID userId = jwtAuthUtil.getCurrentUserId();

        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for(Notification notification : notifications) {
            if(!notification.isRead()) {
                notification.setRead(true);
                notificationRepository.saveAndFlush(notification);
            }
        }
    }

    @Override
    @Transactional
    public void notifyAdmins(NotificationType notificationType, UUID referenceId, String message, UUID actorId) {
        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException(actorId.toString(), "Actor"));

        List<User> admins = userRepository.findByRole(Role.ADMIN);
        for(User user : admins) {
            if(user.getId().equals(actorId)) continue;
            Notification notification = Notification.builder()
                    .user(user)
                    .notificationType(notificationType)
                    .message(message)
                    .referenceId(referenceId)
                    .actor(actor)
                    .build();
            Notification saved = notificationRepository.save(notification);
        }

        List<User> superAdmins = userRepository.findByRole(Role.SUPER_ADMIN);
        for(User user : superAdmins) {
            if(user.getId().equals(actorId)) continue;
            Notification notification = Notification.builder()
                    .user(user)
                    .notificationType(notificationType)
                    .message(message)
                    .referenceId(referenceId)
                    .actor(actor)
                    .build();
            Notification saved = notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void notifySuperAdmins(NotificationType notificationType, UUID referenceId, String message, UUID actorId) {
        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException(actorId.toString(), "Actor"));

        List<User> superAdmins = userRepository.findByRole(Role.SUPER_ADMIN);
        for(User user : superAdmins) {
            if(user.getId().equals(actorId)) continue;
            Notification notification = Notification.builder()
                    .user(user)
                    .notificationType(notificationType)
                    .message(message)
                    .referenceId(referenceId)
                    .actor(actor)
                    .build();
            Notification saved = notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void notifyUser(NotificationType notificationType, UUID referenceId, String message, UUID actorId, UUID recipientId) {
        User actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException(actorId.toString(), "Actor"));

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException(recipientId.toString(), "Recipient"));

        Notification notification = Notification.builder()
                .user(recipient)
                .notificationType(notificationType)
                .message(message)
                .referenceId(referenceId)
                .actor(actor)
                .build();
        Notification saved = notificationRepository.save(notification);
    }

}
