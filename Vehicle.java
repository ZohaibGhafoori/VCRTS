public class Vehicle {

    // Vehicle information
    private String vehicleID;
    private VehicleOwner owner;
    private String vehicleInfo;
    private String vehicleType;
    private double residencyTime;
    private String status;
    private String arrivalTime;
    private String departureTime;

    // Constructor
    public Vehicle(String vehicleID, String vehicleInfo, String vehicleType,
                   double residencyTime, String arrivalTime,
                   String departureTime) {

        this.vehicleID = vehicleID;
        this.vehicleInfo = vehicleInfo;
        this.vehicleType = vehicleType;
        this.residencyTime = residencyTime;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.status = "Available";
    }

    // Get Vehicle ID
    public String getVehicleID() {
        return vehicleID;
    }

    // Get the owner of the vehicle
    public VehicleOwner getOwner() {
        return owner;
    }

    // Set the owner of the vehicle
    public void setOwner(VehicleOwner owner) {
        this.owner = owner;
    }

    // Get Vehicle Information
    public String getVehicleInfo() {
        return vehicleInfo;
    }

    // Get Vehicle Type
    public String getVehicleType() {
        return vehicleType;
    }

    // Get Residency Time
    public double getResidencyTime() {
        return residencyTime;
    }

    // Get Vehicle Status
    public String getStatus() {
        return status;
    }

    // Update vehicle information
    public void setVehicleInfo(String newInfo) {
        vehicleInfo = newInfo;
    }


    public void startJob() {
        status = "Busy";
    }

    // Create's checkpoint
    public void createCheckpoint() {
        System.out.println("Checkpoint requested");
    }

 
    public void eraseData() {
        status = "Available";
    }
}
