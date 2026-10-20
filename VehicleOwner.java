public class VehicleOwner {

    // Owner information
    private String ownerID;
    private String name;

    public VehicleOwner(String ownerID, String name) {
        this.ownerID = ownerID;
        this.name = name;
    }

  
    public String getOwnerID() {
        return ownerID;
    }


    public String getName() {
        return name;
    }

    // Rent a vehicle
    public void rentVehicle() {
        System.out.println("Owner registered a vehicle");
    }

    // Update's vehicle information
    public void updateVehicleInfo() {
        System.out.println("Vehicle information updated");
    }
}
