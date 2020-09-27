package com.opinta.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
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
    private float weight;
    private BigDecimal declaredPrice;
    private BigDecimal price;
    private BigDecimal postPay;
    private String description;

    @OneToMany
    @JoinColumn(name = "parcel_id")
    private List<Parcel> parcels;

    @Transient
    private int parcelsQuantity;

    public Shipment() {
        this.parcels = new ArrayList<>();
        this.parcelsQuantity = parcels.size();
    }

    public Shipment(Client sender, Client recipient, DeliveryType deliveryType, BigDecimal price, BigDecimal postPay) {
        this.sender = sender;
        this.recipient = recipient;
        this.deliveryType = deliveryType;
        this.price = price;
        this.postPay = postPay;
        this.parcels = new ArrayList<>();
        this.parcelsQuantity = parcels.size();
    }

    public BigDecimal getPrice() {
        if (parcelListSizeChanged()) {
            this.price = calculatePrice();
        }
        return this.price;
    }

    public BigDecimal getDeclaredPrice() {
        if (parcelListSizeChanged()) {
            this.declaredPrice = calculateDeclaredPrice();
        }
        return this.declaredPrice;
    }

    public float getWeight(){
        if (parcelListSizeChanged()) {
            this.weight = calculateWeight();
        }
        return this.weight;
    }

    public BigDecimal calculatePrice() {
        return parcels.stream()
                .map(Parcel::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateDeclaredPrice() {
        return parcels.stream()
                .map(Parcel::getDeclaredPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public float calculateWeight() {
        return parcels.stream()
                .map(Parcel::getWeight)
                .reduce(0f, Float::sum);
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
