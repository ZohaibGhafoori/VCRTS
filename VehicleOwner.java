public class VehicleOwner {

    // Owner information
    private String ownerID;
    private String name;
    private Vehicle vehicle;

    // Constructor
    public VehicleOwner(String ownerID, String name) {
        this.ownerID = ownerID;
        this.name = name;
    }

    // Get Owner ID
    public String getOwnerID() {
        return ownerID;
    }

    // Get Owner Name
    public String getName() {
        return name;
    }

    // Rent a vehicle
    public void rentVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        System.out.println("Owner registered a vehicle");
    }

    // Get the owner's vehicle
    public Vehicle getVehicle() {
        return vehicle;
    }

    // Update vehicle information
    public void updateVehicleInfo(String newInfo) {
        if (vehicle != null) {
            vehicle.setVehicleInfo(newInfo);
        }
    }
}
