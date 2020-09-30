package com.opinta.dao;

import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;

import java.util.List;

public interface ParcelItemDao {

    List<ParcelItem> getAllParcelItemsByParcel (Parcel parcel);

    ParcelItem getById(long id);

    ParcelItem save(ParcelItem shipment);

    void update(ParcelItem shipment);

    void delete(ParcelItem shipment);
}
