package com.opinta.controller;

import com.opinta.dto.ParcelDto;
import com.opinta.dto.ParcelItemDto;
import com.opinta.dto.ShipmentDto;
import com.opinta.entity.ParcelItem;
import com.opinta.service.ParcelItemService;
import com.opinta.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/parcels")
public class ParcelController {

    private final ParcelService parcelService;
    private final ParcelItemService parcelItemService;

    @Autowired
    public ParcelController(ParcelService parcelService, ParcelItemService parcelItemService) {
        this.parcelService = parcelService;
        this.parcelItemService = parcelItemService;
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<ParcelDto> getParcels() {
        return parcelService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getShipment(@PathVariable("id") long id) {
        ParcelDto parcelDto = parcelService.getById(id);
        if (parcelDto == null) {
            return new ResponseEntity<>(format("No Parcel found for ID %d", id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelDto, OK);
    }

    @GetMapping("{id}/parcel-items")
    public ResponseEntity<?> getParcelItems(@PathVariable("id") long id) {
        List<ParcelItemDto> parcelItemDtos = parcelItemService.getAllParcelItemsByParcelId(id);
        if (parcelItemDtos == null) {
            return new ResponseEntity<>(format("Parcel %d doesn't exist", id), NOT_FOUND);
        }
        return new ResponseEntity<>(parcelItemDtos, OK);
    }
}
