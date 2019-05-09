import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchAlgorithm {

    private int[] indexNeighbourArr;

    void provideSearchDirections(Map<Integer, Agent> agentList, Map<String, Hexagon> hexagonMap) {

        int[] sixNeigbourIndices = new int[6];
        for (int idx = 0; idx < 6; idx++)
            sixNeigbourIndices[idx] = idx;

        for (int i = 0; i < agentList.size(); i++) {

            Agent agent = agentList.get(i);

            if (agent.status != 0) {
                agent.updateAgentMovement();
                continue;
            }

            String agentHexId = agent.currentHexId;
            Hexagon agentHexagon = hexagonMap.get(agentHexId);

            if (agentHexagon != null && agentHexagon.getNeighbours() != null) {

                List<String> adjacentHexagons = agentHexagon.getNeighbours().get(0);

                if (adjacentHexagons.size() == 6)
                    indexNeighbourArr = sixNeigbourIndices;
                else
                    for (int l = 0; l < adjacentHexagons.size(); l++)
                        indexNeighbourArr[l] = l;

                Map<String, Integer> adjacentHexagonWeights = new HashMap<>();
                String current_time = agentList.get(i).currentTime;

                try {
                    current_time = Helper.convertToClosestTimeInterval(Statistics.currTime);
                } catch (ParseException ex) {

                }

                for (int j = 0; j < adjacentHexagons.size(); j++) {
                    String neighbour = adjacentHexagons.get(j);
                    if (hexagonMap.get(neighbour) != null && hexagonMap.get(neighbour).getWeightsByTime() != null) {
                        adjacentHexagonWeights.put(neighbour,
                                (hexagonMap.get(neighbour).getWeightsByTime().getOrDefault(current_time, 0)) + 1);
                    } else
                        adjacentHexagonWeights.put(neighbour, 1);
                }

                int chosenIndex = AdjacentHexPicker.pickTheBest(indexNeighbourArr, adjacentHexagonWeights, adjacentHexagons);

                String destination_hex_id = adjacentHexagons.get(chosenIndex);
                if (hexagonMap.get(destination_hex_id) != null) {

                    Location destination = hexagonMap.get(adjacentHexagons.get(chosenIndex)).center;

                    double travelTime = Graphhopper.time(agentHexagon.center.latitude, agentHexagon.center.longitude, destination.latitude, destination.longitude) / Statistics.ONE_MINUTE_IN_MILLIS;

                    agent.setDestination(destination_hex_id, 0, travelTime, 2);
//                System.out.println("travel time: " + agent.currentTravelTime);
                }
            }
        }
    }
}

