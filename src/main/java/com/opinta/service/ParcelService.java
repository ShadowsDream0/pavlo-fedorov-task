package com.opinta.service;

import com.opinta.dto.ParcelDto;
import com.opinta.entity.Parcel;
import com.opinta.entity.Shipment;

import java.util.List;

public interface ParcelService {

    List<ParcelDto> getAllParcelsByShipmentId(long shipmentId);

    List<ParcelDto> getAll();

    List<Parcel> getAllEntities();

    Parcel getEntityById(long id);

    Parcel saveEntity(Parcel parcel);

    ParcelDto getById(long id);

    ParcelDto save(ParcelDto parcelDto);

    ParcelDto update(long id, ParcelDto parcelDto);

    boolean delete(long id);
}
