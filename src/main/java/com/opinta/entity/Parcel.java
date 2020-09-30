package com.opinta.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(exclude = "shipment")
@Table(name = "parcel")
public class Parcel {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    private float weight;
    private float length;
    private float width;
    private float height;
    private BigDecimal declaredPrice;
    private BigDecimal price;

    @ManyToMany(mappedBy = "parcels", cascade = CascadeType.ALL)
    private Set<ParcelItem> parcelItems = new HashSet<>();


    public Parcel() {}

    public Parcel(final float weight, final float length, final float width, final float height,
                  @NonNull final BigDecimal declaredPrice, @NonNull final BigDecimal price) {
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.declaredPrice = declaredPrice;
        this.price = price;
    }
}
