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

    // Zohaib - Check Owner information
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
}
