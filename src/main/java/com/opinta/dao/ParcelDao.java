package com.opinta.dao;

import com.opinta.entity.Parcel;


public interface ParcelDao {
    Parcel getById(long id);

    Parcel save(Parcel shipment);

    void update(Parcel shipment);

    void delete(Parcel shipment);
}
