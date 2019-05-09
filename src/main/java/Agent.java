import java.util.Objects;

public class Agent {

    int agentId = -1;
    Location currentLocation;
    String currentHexId;
    String currentTime;
    int status;
    String destinationHexId;
    double destinationDistance;
    double currentTravelTime;
    double penalty;
    double totalSearchingTime;

    Agent(int agentId, String currentHexId, String currentTime, Location currentLocation) {
        this.agentId = agentId;
        this.currentHexId = currentHexId;
        this.currentTime = currentTime;
        this.currentLocation = currentLocation;
        this.status = 0;
        this.currentLocation = currentLocation;
        this.currentTravelTime = 0;
        this.destinationDistance = 0;
        this.penalty = 0;
        this.totalSearchingTime = 0;
    }

    void setDestination(String hex_id, double destinationDistance, double travelTime, int status) {
        this.destinationHexId = hex_id;
        this.destinationDistance = destinationDistance;
        this.currentTravelTime = travelTime;
        this.status = status;
    }

    void updateAgentMovement() {

        if (currentTravelTime > Statistics.incrementalTimeWindow) { // still moving
            currentTravelTime -= Statistics.incrementalTimeWindow;
            if (status == 2) {
//                System.out.println("incrementing search time");
                Statistics.accumulatedSearchTime += Statistics.incrementalTimeWindow; // moving in search not in journey
            }
        } else { // reached destination, stopped moving
            currentTravelTime = 0;
            status = 0;
            teleportAgentToDestinationHex();
            destinationHexId = null;
            AgentGeneration.freeAgentList.put(agentId, this);
//            System.out.println("Added Agent back to Free List: " + agentId + " status:  " + status);
        }

    }

    void teleportAgentToDestinationHex() {
        if (Main.hexagonMap.get(destinationHexId) != null) {
            Main.hexagonMap.get(currentHexId).agentsPresent.remove(this);
            Main.hexagonMap.get(destinationHexId).agentsPresent.add(this);
            currentHexId = destinationHexId;
        } else {
            Main.hexagonMap.get(currentHexId).agentsPresent.remove(this);
            Main.hexagonMap.get(currentHexId).agentsPresent.add(this);
        }
    }
}