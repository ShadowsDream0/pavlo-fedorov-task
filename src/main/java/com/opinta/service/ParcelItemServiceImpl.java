package com.opinta.service;

import com.opinta.dao.ParcelItemDao;
import com.opinta.dto.ParcelItemDto;
import com.opinta.entity.Parcel;
import com.opinta.entity.ParcelItem;
import com.opinta.mapper.ParcelItemMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

@Service
@Slf4j
public class ParcelItemServiceImpl implements ParcelItemService {
    private ParcelItemDao parcelItemDao;
    private ParcelItemMapper parcelItemMapper;

    @Autowired
    public ParcelItemServiceImpl(ParcelItemDao parcelItemDao, ParcelItemMapper parcelItemMapper) {
        this.parcelItemDao = parcelItemDao;
        this.parcelItemMapper = parcelItemMapper;
    }

    @Override
    @Transactional
    public ParcelItem getEntityById(final long id) {
        log.info("Getting ParcelItem by id {}", id);
        return parcelItemDao.getById(id);
    }

    @Override
    @Transactional
    public ParcelItem saveEntity(@NonNull final ParcelItem parcelItem) {
        log.info("Saving parcelItem {}", parcelItem);
        return parcelItemDao.save(parcelItem);
    }

    @Override
    @Transactional
    public ParcelItemDto getById(final long id) {
        return parcelItemMapper.toDto(getEntityById(id));
    }

    @Override
    @Transactional
    public ParcelItemDto save(@NonNull final ParcelItemDto parcelItemDto) {
        ParcelItem parcelItem = parcelItemMapper.toEntity(parcelItemDto);

        log.info("Saving parcel item: {} ", parcelItem);

        return parcelItemMapper.toDto(parcelItemDao.save(parcelItem));
    }

    @Override
    @Transactional
    public ParcelItemDto update(final long id, @NonNull final ParcelItemDto parcelItemDto) {
        ParcelItem source = parcelItemMapper.toEntity(parcelItemDto);
        ParcelItem target = parcelItemDao.getById(id);
        if (target == null) {
            log.debug("Can't update parcel item. Parcel item doesn't exist {}", id);
            return null;
        }
        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for parcel item", e);
        }
        target.setId(id);
        log.info("Updating parcel item {}", target);

        parcelItemDao.update(target);
        return parcelItemMapper.toDto(target);
    }

    @Override
    @Transactional
    public boolean delete(final long id) {
        ParcelItem parcelItem = parcelItemDao.getById(id);
        if (parcelItem == null) {
            log.debug("Can't delete parcel item. Parcel item doesn't exist {}", id);
            return false;
        }
        parcelItem.setId(id);
        log.info("Deleting parcel item {}", parcelItem);
        parcelItemDao.delete(parcelItem);
        return true;
    }
}
