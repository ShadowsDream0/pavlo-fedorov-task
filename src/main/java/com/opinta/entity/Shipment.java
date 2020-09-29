package com.opinta.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Data
@Table(name = "shipment")
public class Shipment {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Client sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Client recipient;
    @OneToOne
    private BarcodeInnerNumber barcode;
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;
    private BigDecimal price;
    private BigDecimal postPay;
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shipment", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Parcel> parcels = new ArrayList<>();

    @Transient
    private int parcelsQuantity;

    public Shipment() {
        this.parcelsQuantity = parcels.size();
        this.price = calculatePrice();
    }

    public Shipment(Client sender, Client recipient, DeliveryType deliveryType, BigDecimal price, BigDecimal postPay) {
        this.sender = sender;
        this.recipient = recipient;
        this.deliveryType = deliveryType;
        this.price = price;
        this.postPay = postPay;
        this.parcelsQuantity = parcels.size();
        this.price = calculatePrice();
    }

    public void addParcel(@NonNull final Parcel parcel) {
        parcel.setShipment(this);
        this.parcels.add(parcel);
    }

    public BigDecimal getPrice() {
        if (parcelListSizeChanged()) {
            this.price = calculatePrice();
        }
        return this.price;
    }

    public BigDecimal calculatePrice() {
        Objects.requireNonNull(parcels, "field parcels must not be null but was null");
        return parcels.stream()
                .filter(Objects::nonNull)
                .map(Parcel::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean parcelListSizeChanged() {
        Objects.requireNonNull(parcels, "field parcels must not be null but was null");
        int size = parcels.size();
        if (parcelsQuantity != size) {
            parcelsQuantity = size;
            return true;
        }
        return false;
    }
}
