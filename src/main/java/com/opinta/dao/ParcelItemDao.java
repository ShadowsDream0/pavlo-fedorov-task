package com.opinta.dao;

import com.opinta.entity.ParcelItem;

public interface ParcelItemDao {
    ParcelItem getById(long id);

    ParcelItem save(ParcelItem shipment);

    void update(ParcelItem shipment);

    void delete(ParcelItem shipment);
}
