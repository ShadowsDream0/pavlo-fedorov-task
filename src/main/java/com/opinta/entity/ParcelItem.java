package com.opinta.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class ParcelItem {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private long quantity;
    private float weight;
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "parcel_item",
                joinColumns = @JoinColumn(name = "parcel_id"),
                inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Parcel> parcels;
}
