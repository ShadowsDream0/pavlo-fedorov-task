package integration.helper;

import com.opinta.entity.*;
import com.opinta.service.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class TestHelper {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private CounterpartyService counterpartyService;
    @Autowired
    private PostcodePoolService postcodePoolService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private PostOfficeService postOfficeService;
    @Autowired
    private ParcelService parcelService;

    public PostOffice createPostOffice() {
        PostOffice postOffice = new PostOffice("Lviv post office", createAddress(), createPostcodePool());
        return postOfficeService.saveEntity(postOffice);
    }

    public void deletePostOffice(PostOffice postOffice) {
        postOfficeService.delete(postOffice.getId());
        postcodePoolService.delete(postOffice.getPostcodePool().getId());
    }

    public Shipment createShipment() {
        Shipment shipment = new Shipment(createClient(), createClient(),
                DeliveryType.D2D, new BigDecimal(30), new BigDecimal(35.2));
        shipment.setParcels(createParcels(shipment));

        return shipmentService.saveEntity(shipment);
    }

    public void deleteShipment(Shipment shipment) {
        shipmentService.delete(shipment.getId());
        clientService.delete(shipment.getSender().getId());
        clientService.delete(shipment.getRecipient().getId());
    }

    public Client createClient() {
        Client newClient = new Client("FOP Ivanov", "001", createAddress(), createCounterparty());
        return clientService.saveEntity(newClient);
    }

    public void deleteClient(Client client) {
        clientService.delete(client.getId());
        addressService.delete(client.getAddress().getId());
        deleteCounterpartyWithPostcodePool(client.getCounterparty());
    }

    public Address createAddress() {
        Address address = new Address("00001", "Ternopil", "Monastiriska",
                "Monastiriska", "Sadova", "51", "");
        return addressService.saveEntity(address);
    }

    public Counterparty createCounterparty() {
        Counterparty counterparty = new Counterparty("Modna kasta", createPostcodePool());
        return counterpartyService.saveEntity(counterparty);
    }

    public PostcodePool createPostcodePool() {
        return postcodePoolService.saveEntity(new PostcodePool("12345", false));
    }

    public void deleteCounterpartyWithPostcodePool(Counterparty counterparty) {
        counterpartyService.delete(counterparty.getId());
        postcodePoolService.delete(counterparty.getPostcodePool().getId());
    }

    public JSONObject getJsonObjectFromFile(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(new FileReader(getFileFromResources(filePath)));
    }

    public String getJsonFromFile(String filePath) throws IOException, ParseException {
        return getJsonObjectFromFile(filePath).toString();
    }

    public File getFileFromResources(String path) {
        return new File(getClass().getClassLoader().getResource(path).getFile());
    }

    private List<Parcel> createParcels(Shipment shipment) {
        List<Parcel> parcels = new ArrayList<>();

        Parcel parcel1 = new Parcel(0.4f, 29.0f, 3.0f, 0.5f, BigDecimal.valueOf(20.0f),
                BigDecimal.valueOf(15.0f));
        parcel1.setShipment(shipment);
        parcel1.setParcelItems(createParcelItems());

        Parcel parcel2 = new Parcel(14.0f, 69.0f, 4.0f, 1.0f, BigDecimal.valueOf(100.0f),
                BigDecimal.valueOf(30.0f));
        parcel2.setShipment(shipment);
        parcel2.setParcelItems(createParcelItems());

        parcels.add(parcel1);
        parcels.add(parcel2);

        return parcels;
    }

    private void deleteParcel(Parcel parcel) {
        parcelService.delete(parcel.getId());
    }

    private Set<ParcelItem> createParcelItems() {
        ParcelItem parcelItem1 = new ParcelItem("test1", 1, 1.0f, BigDecimal.valueOf(10.0f));
        ParcelItem parcelItem2 = new ParcelItem("test2", 2, 2.0f, BigDecimal.valueOf(20.0f));
        ParcelItem parcelItem3 = new ParcelItem("test3", 3, 3.0f, BigDecimal.valueOf(30.0f));

        Set<ParcelItem> parcelItems = new HashSet<>();

        parcelItems.add(parcelItem1);
        parcelItems.add(parcelItem2);
        parcelItems.add(parcelItem3);

        return parcelItems;
    }
}
