public class OwnerRegistrationData {

    // Zohaib - Owner information
    private String ownerID;
    private String plate;
    private String model;
    private String type;
    private double time;

    // Constructor
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

    // Check Owner information
    public boolean isValid() {

        if (ownerID == null || ownerID.trim().isEmpty()) {
            return false;
        }

        if (plate == null || plate.trim().isEmpty()) {
            return false;
        }

        if (model == null || model.trim().isEmpty()) {
            return false;
        }

        if (type == null || type.trim().isEmpty()) {
            return false;
        }

        if (!Double.isFinite(time) || time <= 0) {
            return false;
        }

        return true;
    }

    // Prepare Owner information for server
    public String getRecord() {

        String record = "";

        record += "Owner ID: " + ownerID.trim() + "\n";
        record += "License Plate: " + plate.trim() + "\n";
        record += "Vehicle Model: " + model.trim() + "\n";
        record += "Vehicle Type: " + type.trim() + "\n";
        record += "Residency Time: " + time + " hours\n";

        return record;
    }
}
