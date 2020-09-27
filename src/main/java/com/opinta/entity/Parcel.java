package com.opinta.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Parcel {
    @Id
    @GeneratedValue
    private long id;

/*    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;*/

    private float weight;
    private float length;
    private float width;
    private float height;
    private BigDecimal declaredPrice;
    private BigDecimal price;

    @ManyToMany(mappedBy = "parcels", fetch = FetchType.LAZY)
    private List<ParcelItem> parcelItems;


    public Parcel() {
        parcelItems = new ArrayList<>();
    }

    public Parcel(final float weight, final float length, final float width, final float height,
                  @NonNull final BigDecimal declaredPrice, @NonNull final BigDecimal price) {
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.declaredPrice = declaredPrice;
        this.price = price;
        parcelItems = new ArrayList<>();
    }
}
