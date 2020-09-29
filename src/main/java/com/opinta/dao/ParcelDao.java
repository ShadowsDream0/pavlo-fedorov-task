package com.opinta.dao;

import com.opinta.entity.Parcel;

import java.util.List;

public interface ParcelDao {
    Parcel getById(long id);

    List<Parcel> getAllParcelsByShipmentId(long shipmentId);

    Parcel save(Parcel shipment);

    void update(Parcel shipment);

    void delete(Parcel shipment);
}
