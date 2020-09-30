package com.opinta.dao;

import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParcelItemDaoImpl implements ParcelItemDao {
    private final SessionFactory sessionFactory;
    private static final String SELECT_PARCEL_ITEMS_QUERY = "SELECT name, quantity, weight, price" +
                                                            " FROM item" +
                                                            " JOIN parcel_items" +
                                                                " ON item.id = parcel_items.item_id" +
                                                            " JOIN parcel" +
                                                                " ON parcel.id = parcel_items.parcel_id" +
                                                            " WHERE parcel_items.parcel_id = ";

    @Autowired
    public ParcelItemDaoImpl(@NonNull final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ParcelItem> getAllParcelItemsByParcel(Parcel parcel) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createSQLQuery(SELECT_PARCEL_ITEMS_QUERY + parcel.getId())
                .list();
    }

    @Override
    public ParcelItem getById(final long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (ParcelItem) session.get(ParcelItem.class, id);
    }

    @Override
    public ParcelItem save(@NonNull final ParcelItem parcelItem) {
        final Session session = sessionFactory.getCurrentSession();
        return (ParcelItem) session.merge(parcelItem);
    }

    @Override
    public void update(ParcelItem parcelItem) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(parcelItem);
    }

    @Override
    public void delete(ParcelItem parcelItem) {
        final Session session = sessionFactory.getCurrentSession();
        session.delete(parcelItem);
    }
}
