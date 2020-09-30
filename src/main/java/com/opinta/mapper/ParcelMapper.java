package com.opinta.mapper;

import com.opinta.dto.ParcelDto;
import com.opinta.entity.Parcel;
import com.opinta.entity.Shipment;
import com.opinta.service.ParcelService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParcelMapper extends BaseMapper<ParcelDto, Parcel> {

    @Override
    @Mapping(source = "shipment.id", target = "shipmentId")
    ParcelDto toDto(Parcel parcel);

    @Override
    @Mapping(target = "shipment", expression = "java(getShipment(parcelDto.getShipmentId()))")
    Parcel toEntity(ParcelDto parcelDto);

    default Shipment getShipment(long id) {
        Shipment shipment = new Shipment();
        shipment.setId(id);
        return shipment;
    }
}
