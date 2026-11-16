public class OwnerRegistrationData {

    // Zohaib - Owner registration information
    private String ownerID;
    private String plate;
    private String model;
    private String type;
    private double time;

    public OwnerRegistrationData(
            String ownerID,
            String plate,
            String model,
            String type,
            double time) {

        this.ownerID = ownerID;
        this.plate = plate;
        this.model = model;
        this.type = type;
        this.time = time;
    }
}
