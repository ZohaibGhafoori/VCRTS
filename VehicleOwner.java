import java.util.ArrayList;

public class VehicleOwner extends User {

    // An owner can rent out one or more vehicles
    private ArrayList<Vehicle> vehicles;

    // Constructor
    public VehicleOwner(String ownerID) {
        super(ownerID);
        vehicles = new ArrayList<Vehicle>();
    }

    public String getUserType() {
        return "Owner";
    }

    // Rent a vehicle and link it back to this owner
    public void rentVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setOwner(this);
        System.out.println("Owner registered a vehicle");
    }

    // Get the owner's vehicles
    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    // Update the model of one of the owner's vehicles
    public void updateVehicleModel(String vehicleID, String newModel) {
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);

            if (vehicle.getVehicleID().equals(vehicleID)) {
                vehicle.setVehicleModel(newModel);
            }
        }
    }
}
