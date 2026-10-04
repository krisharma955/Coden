package com.k955.Coden.mapper;

import com.k955.Coden.dtos.Snippet.SnippetResponse;
import com.k955.Coden.dtos.Snippet.SnippetView;
import com.k955.Coden.entity.Snippet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnippetMapper {

    SnippetResponse toSnippetResponse(Snippet snippet);

    SnippetView toSnippetView(Snippet snippet);

}
