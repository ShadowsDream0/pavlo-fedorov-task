package com.opinta.mapper;

import com.opinta.dto.ParcelDto;
import com.opinta.entity.Parcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelMapper extends BaseMapper<ParcelDto, Parcel> {

    @Override
    @Mapping(source = "shipment.id", target = "shipmentId")
    ParcelDto toDto(Parcel parcel);

    @Override
    @Mapping(source = "shipmentId", target = "shipment.id")
    Parcel toEntity(ParcelDto dto);
}
