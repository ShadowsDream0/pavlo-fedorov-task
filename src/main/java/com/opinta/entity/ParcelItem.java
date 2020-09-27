package com.opinta.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    @ManyToMany(mappedBy = "parcel_items")
    private List<Parcel> parcels;
}
