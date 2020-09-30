package com.opinta.service;

import com.opinta.dao.ParcelDao;
import com.opinta.dao.ShipmentDao;
import com.opinta.dao.TariffGridDao;
import com.opinta.dto.ParcelDto;
import com.opinta.dto.ShipmentDto;
import com.opinta.entity.*;
import com.opinta.mapper.ParcelMapper;
import com.opinta.util.AddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

@Service
@Slf4j
public class ParcelServiceImpl implements ParcelService {

    private final ParcelMapper parcelMapper;
    private final TariffGridDao tariffGridDao;
    private final ParcelDao parcelDao;
    private final ShipmentDao shipmentDao;
    private final ShipmentService shipmentService;


    @Autowired
    ParcelServiceImpl(final ParcelMapper parcelMapper, final TariffGridDao tariffGridDao, final ParcelDao parcelDao,
                      final ShipmentService shipmentService, final ShipmentDao shipmentDao) {
        this.parcelMapper = parcelMapper;
        this.tariffGridDao = tariffGridDao;
        this.parcelDao = parcelDao;
        this.shipmentService = shipmentService;
        this.shipmentDao = shipmentDao;
    }

    @Override
    @Transactional
    public List<ParcelDto> getAllParcelsByShipmentId(long shipmentId) {
        Shipment shipment = shipmentDao.getById(shipmentId);
        if (shipment == null) {
            log.debug("Can't get parcel list by shipment. Client {} doesn't exist", shipmentId);
            return null;
        }
        log.info("Getting all parcels by shipment {}", shipment);
        return parcelMapper.toDto(parcelDao.getAllParcelsByShipment(shipment));
    }

    @Override
    @Transactional
    public List<ParcelDto> getAll() {
        return parcelMapper.toDto(getAllEntities());
    }

    @Override
    @Transactional
    public List<Parcel> getAllEntities() {
        log.info("Getting all parcels");
        return parcelDao.getAll();
    }

    @Override
    @Transactional
    public Parcel getEntityById(final long id) {
        log.info("Getting Parcel by id {}", id);
        return parcelDao.getById(id);
    }

    @Override
    @Transactional
    public Parcel saveEntity(final Parcel parcel) {
        log.info("Saving parcel {}", parcel);
        return parcelDao.save(parcel);
    }

    @Override
    @Transactional
    public ParcelDto getById(final long id) {
        return parcelMapper.toDto(getEntityById(id));
    }

    @Override
    @Transactional
    public ParcelDto save(final ParcelDto parcelDto) {
        Parcel parcel = parcelMapper.toEntity(parcelDto);

        parcel.setPrice(calculateParcelPrice(parcelDto));

        log.info("Saving parcel: {} ", parcel);

        return parcelMapper.toDto(parcelDao.save(parcel));
    }

    @Override
    @Transactional
    public ParcelDto update(long id, ParcelDto parcelDto) {
        Parcel source = parcelMapper.toEntity(parcelDto);
        Parcel target = parcelDao.getById(id);
        if (target == null) {
            log.debug("Can't update parcel. Parcel doesn't exist {}", id);
            return null;
        }
        try {
            copyProperties(target, source);
        } catch (Exception e) {
            log.error("Can't get properties from object to updatable object for parcel", e);
        }
        target.setId(id);
        target.setPrice(calculateParcelPrice(parcelDto));
        log.info("Updating parcel {}", target);
        parcelDao.update(target);
        return parcelMapper.toDto(target);
    }

    @Override
    @Transactional
    public boolean delete(long id) {
        Parcel parcel = parcelDao.getById(id);
        if (parcel == null) {
            log.debug("Can't delete parcel. Parcel doesn't exist {}", id);
            return false;
        }
        parcel.setId(id);
        log.info("Deleting parcel {}", parcel);
        parcelDao.delete(parcel);
        return true;
    }

    private BigDecimal calculateParcelPrice(final ParcelDto parcelDto) {

        final Parcel parcel = parcelMapper.toEntity(parcelDto);
        log.info("Calculating price for parcel {}", parcel);

        final Shipment shipment = shipmentService.getEntityById(parcelDto.getShipmentId());
        final Address senderAddress = shipment.getSender().getAddress();
        final Address recipientAddress = shipment.getRecipient().getAddress();
        W2wVariation w2wVariation = W2wVariation.COUNTRY;
        if (AddressUtil.isSameTown(senderAddress, recipientAddress)) {
            w2wVariation = W2wVariation.TOWN;
        } else if (AddressUtil.isSameRegion(senderAddress, recipientAddress)) {
            w2wVariation = W2wVariation.REGION;
        }

        TariffGrid tariffGrid = tariffGridDao.getLast(w2wVariation);
        if (parcel.getWeight() < tariffGrid.getWeight() &&
                parcel.getLength() < tariffGrid.getLength()) {
            tariffGrid = tariffGridDao.getByDimension(parcel.getWeight(), parcel.getLength(), w2wVariation);
        }

        log.info("TariffGrid for weight {} per length {} and type {}: {}",
                parcel.getWeight(), parcel.getLength(), w2wVariation, tariffGrid);

        if (tariffGrid == null) {
            return BigDecimal.ZERO;
        }

        final float price = tariffGrid.getPrice() + getSurcharges(shipment);

        return new BigDecimal(Float.toString(price));
    }

    private float getSurcharges(final Shipment shipment) {
        float surcharges = 0;
        if (shipment.getDeliveryType().equals(DeliveryType.D2W) ||
                shipment.getDeliveryType().equals(DeliveryType.W2D)) {
            surcharges += 9;
        } else if (shipment.getDeliveryType().equals(DeliveryType.D2D)) {
            surcharges += 12;
        }
        return surcharges;
    }
}
