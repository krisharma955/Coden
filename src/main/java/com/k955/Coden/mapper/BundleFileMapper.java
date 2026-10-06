package com.k955.Coden.mapper;

import com.k955.Coden.dtos.BundleFile.BundleFileResponse;
import com.k955.Coden.entity.BundleFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BundleFileMapper {

    BundleFileResponse toBundleFileResponse(BundleFile bundleFile);

}
