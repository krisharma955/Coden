package com.k955.Coden.mapper;

import com.k955.Coden.dtos.Folder.FolderResponse;
import com.k955.Coden.entity.Folder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FolderMapper {

    FolderResponse toFolderResponse(Folder folder);

}
