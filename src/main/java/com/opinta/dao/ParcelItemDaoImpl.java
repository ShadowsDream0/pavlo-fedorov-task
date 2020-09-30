package com.opinta.dao;

import com.opinta.entity.ParcelItem;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ParcelItemDaoImpl implements ParcelItemDao {
    SessionFactory sessionFactory;

    @Autowired
    public ParcelItemDaoImpl(@NonNull final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
