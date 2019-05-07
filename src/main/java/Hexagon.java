import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Hexagon {

    Location center;
    private List<List<String>> kRingHexList;
    private Map<String, Integer> weightsByTime;
    List<Agent> agentsPresent;
    String hexId;

    int day_of_week;

    Hexagon(String hexId, int day_of_week, List<List<String>> kRingHexList, double latitude, double longitude) {

        this.hexId = hexId;
        this.kRingHexList = kRingHexList;
        this.day_of_week = day_of_week;

        this.center = new Location();
        this.center.latitude = latitude;
        this.center.longitude = longitude;

        this.agentsPresent = new ArrayList<>();
    }

    List<List<String>> getNeighbours() {
        return kRingHexList;
    }

    Map<String, Integer> getWeightsByTime() {
        return weightsByTime;
    }

    void setWeightsByTime(Map<String, Integer> weightsByTime) {
        this.weightsByTime = weightsByTime;
    }

}
