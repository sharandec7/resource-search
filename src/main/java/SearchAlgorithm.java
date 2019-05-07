import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SearchAlgorithm {

    private int[] indexNeighbourArr;

    void provideSearchDirections(List<Agent> agentList, Map<String, Hexagon> hexagonMap) {

        int[] sixNeigbourIndices = new int[6];
        for (int idx = 0; idx < 6; idx++)
            sixNeigbourIndices[idx] = idx;

        for (int i = 0; i < agentList.size(); i++) {

            Agent agent = agentList.get(i);

            if (agent.status != 0) {
                agent.updateAgentMovement();
            }


            String agentHexId = agent.currentHexId;
            Hexagon agentHexagon = hexagonMap.get(agentHexId);

            List<String> adjacentHexagons = agentHexagon.getNeighbours().get(0);


            if (adjacentHexagons.size() == 6)
                indexNeighbourArr = sixNeigbourIndices;
            else
                for (int l = 0; l < adjacentHexagons.size(); l++)
                    indexNeighbourArr[l] = l;

            Map<String, Integer> expectedNumbers = new HashMap<>();

            // can be updated to CLOCK TIME
            String current_time = agentList.get(i).currentTime;

            for (int j = 0; j < adjacentHexagons.size(); j++) {
                String neighbour = adjacentHexagons.get(j);
                if (hexagonMap.get(neighbour) != null && hexagonMap.get(neighbour).getWeightsByTime() != null) {
                    expectedNumbers.put(neighbour,
                            (hexagonMap.get(neighbour).getWeightsByTime().getOrDefault(current_time, 0)) + 1);
                } else
                    expectedNumbers.put(neighbour, 1);
            }

            int chosenIndex = RandomNumberGenerator.customizedRandom(indexNeighbourArr, expectedNumbers, adjacentHexagons);
            String destination_hex_id = adjacentHexagons.get(chosenIndex);
            if (hexagonMap.get(destination_hex_id) != null) {

                Location destination = hexagonMap.get(adjacentHexagons.get(chosenIndex)).center;

                int milliSeconds = Graphhopper.time(agentHexagon.center.latitude, agentHexagon.center.longitude, destination.latitude, destination.longitude);

                agent.setDestination(destination_hex_id, 0, milliSeconds, 2);
//                System.out.println("travel time: " + agent.currentTravelTime);
            }
        }
    }
}

