package com.k955.Coden.mapper;

import com.k955.Coden.dtos.Bundle.BundleResponse;
import com.k955.Coden.entity.Bundle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BundleMapper {

    BundleResponse toBundleResponse(Bundle bundle);

}
