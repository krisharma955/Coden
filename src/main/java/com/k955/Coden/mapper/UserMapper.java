package com.k955.Coden.mapper;

import com.k955.Coden.dtos.User.UserProfileResponse;
import com.k955.Coden.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileResponse toUserProfileResponse(User user);

}
