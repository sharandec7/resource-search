import java.util.Date;

public class Resource {

    Location pickupLocation;
    Location dropLocation;
    Date pickupTime;
    Date dropTime;
    String hexagonId;
    double waitTime;
    boolean assigned;

    Resource(Location pickupLocation, Date pickupTime, Location dropLocation, Date dropTime, String hexagonId) {

        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.pickupTime = pickupTime;
        this.dropTime = dropTime;
        this.hexagonId = hexagonId;
        assigned = false;
        this.waitTime = 0;
    }
}
