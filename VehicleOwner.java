public class VehicleOwner extends User {

    // Owner information
    private Vehicle vehicle;

    // Constructor
    public VehicleOwner(String ownerID) {
        super(ownerID);
    }

    public String getUserType() {
        return "Owner";
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
