package com.opinta.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class ParcelItem {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private long quantity;
    private float weight;
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parcel_items",
                joinColumns = @JoinColumn(name = "parcel_item_id"),
                inverseJoinColumns = @JoinColumn(name = "parcel_id"))
    private List<Parcel> parcels;

    public ParcelItem(String name, long quantity, float weight, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
        this.price = price;
    }
}
