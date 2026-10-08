package com.k955.Coden.mapper;

import com.k955.Coden.dtos.Notification.NotificationResponse;
import com.k955.Coden.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toNotificationResponse(Notification notification);

}
