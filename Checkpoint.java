import java.io.File;
import java.time.LocalDateTime;

public class Checkpoint {
    
    // Attributes[cite: 1]
    private String imageId;
    private LocalDateTime timestamp;
    private File stateData; // Representing Blob (or File) from the diagram
    private String sourceVehicleId;

    // Default Constructor
    public Checkpoint() {
    }

    // Parameterized Constructor
    public Checkpoint(String imageId, LocalDateTime timestamp, File stateData, String sourceVehicleId) {
        this.imageId = imageId;
        this.timestamp = timestamp;
        this.stateData = stateData;
        this.sourceVehicleId = sourceVehicleId;
    }

    // Getters and Setters[cite: 1]
    
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public File getStateData() {
        return stateData;
    }

    public void setStateData(File stateData) {
        this.stateData = stateData;
    }

    public String getSourceVehicleId() {
        return sourceVehicleId;
    }

    public void setSourceVehicleId(String sourceVehicleId) {
        this.sourceVehicleId = sourceVehicleId;
    }
}
