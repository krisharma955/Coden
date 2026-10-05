package com.k955.Coden.mapper;

import com.k955.Coden.dtos.SnippetStar.SnippetStarResponse;
import com.k955.Coden.entity.SnippetStar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnippetStarMapper {

    SnippetStarResponse toSnippetStarResponse(SnippetStar snippetStar);

}
