package com.opinta.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "SHIPMENT")
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shipment")
    private List<Parcel> parcels;

    @Transient
    private int parcelsQuantity;

    public Shipment() {
        this.parcels = new ArrayList<>();
        this.parcelsQuantity = parcels.size();
        this.price = calculatePrice();
    }

    public Shipment(Client sender, Client recipient, DeliveryType deliveryType, BigDecimal price, BigDecimal postPay) {
        this.sender = sender;
        this.recipient = recipient;
        this.deliveryType = deliveryType;
        this.price = price;
        this.postPay = postPay;
        this.parcels = new ArrayList<>();
        this.parcelsQuantity = parcels.size();
        this.price = calculatePrice();
    }

    public BigDecimal getPrice() {
        if (parcelListSizeChanged()) {
            this.price = calculatePrice();
        }
        return this.price;
    }

    public BigDecimal calculatePrice() {
        return parcels.stream()
                .filter(Objects::nonNull)
                .map(Parcel::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean parcelListSizeChanged() {
        int size = parcels.size();
        if (parcelsQuantity != size) {
            parcelsQuantity = size;
            return true;
        }
        return false;
    }
}
