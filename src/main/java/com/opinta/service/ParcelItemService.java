package com.opinta.service;

import com.opinta.dto.ParcelDto;
import com.opinta.dto.ParcelItemDto;
import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;

public interface ParcelItemService {
    ParcelItem getEntityById(long id);

    ParcelItem saveEntity(ParcelItem parcelItem);

    ParcelItemDto getById(long id);

    ParcelItemDto save(ParcelItemDto parcelItemDto);

    ParcelItemDto update(long id, ParcelItemDto parcelItemDto);

    boolean delete(long id);
}
