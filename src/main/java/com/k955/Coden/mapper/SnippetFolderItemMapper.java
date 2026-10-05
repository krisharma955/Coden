package com.k955.Coden.mapper;

import com.k955.Coden.dtos.SnippetFolderItem.SnippetFolderItemResponse;
import com.k955.Coden.entity.SnippetFolderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnippetFolderItemMapper {

    SnippetFolderItemResponse toSnippetFolderItemResponse(SnippetFolderItem snippetFolderItem);

}
