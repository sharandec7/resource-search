import java.io.IOException;
import java.util.*;

public class AllocationFramework {

    public Queue<Resource> allocate(Queue<Resource> resources, Map<String, Hexagon> hexagonMap, double MLT) throws IOException {

        int totalResource = resources.size();
//        int cab_count = agentList.size();
        int processedResources = 0;
        int K = 4;

        while (processedResources < totalResource) {

//            System.out.println("count: "+count + " resource size: "+resources.size());
            Resource resource = resources.poll();
//            System.out.println("resource hex: " + resource.hexagonId);

            processedResources++;

            if (resource.waitTime >= MLT) {
                dropResource(resource);
                continue; // resource dropped from queue
            }

            double resourceLat = resource.pickupLocation.latitude;
            double resourceLong = resource.pickupLocation.longitude;

//            System.out.println();
            if (!(resourceLat != 0.0 && resourceLong != 0.0)) {
                Statistics.totalInvalidResources += 1;
                continue;
            }

            if (hexagonMap.get(resource.hexagonId) == null) {
//                System.out.println(resource.hexagonId + " : " + hexagonMap.get(resource.hexagonId));
                Statistics.totalInvalidResources += 1;
                continue;
            }

            Hexagon resourceHexagon = hexagonMap.get(resource.hexagonId);
            List<List<String>> kRingHexagons = resourceHexagon.getNeighbours();
            List<List<Agent>> kRingAgents = new ArrayList<>();


            int numberOfAgentsAroundResource = 0;

            // include the agents from the hexagon where resource popped up
            kRingAgents.add(0, (hexagonMap.get(resource.hexagonId).agentsPresent));
            numberOfAgentsAroundResource += kRingAgents.get(0).size();

            // add surrounding hexagon agents
            for (int index = 1; index <= 4; index++) {
                List<Agent> agentsAtK = new ArrayList<>();
                for (String hexId : kRingHexagons.get(index - 1)) {
                    if (hexagonMap.get(hexId) != null) {
                        agentsAtK.addAll(hexagonMap.get(hexId).agentsPresent);
                        numberOfAgentsAroundResource += hexagonMap.get(hexId).agentsPresent.size();
                    } else {
                        Statistics.outOfBoundsResources += 1;
                    }
                }
                kRingAgents.add(index, agentsAtK);
            }

            int currentRing = 0;
            double timeToPickup = 2147483647;
            Agent closestAgent = null;

            while (currentRing <= K) {

                for (Agent agent : kRingAgents.get(currentRing)) {

//                    int size = kRingAgents.get(currentRing).size();

                    if (agent.status == 1) {
                        continue;
                    }

                    double agentLat = Main.hexagonMap.get(agent.currentHexId).center.latitude;
                    double agentLong = Main.hexagonMap.get(agent.currentHexId).center.longitude;

                    if ((agentLat == 0.0 && agentLong == 0.0)) {
                        System.out.println("Agent location undefined");
                        continue;
                    }

                    if (agent.status != 1) {

                        double currPickupTime = Graphhopper.time(resourceLat, resourceLong, agentLat, agentLong) / Statistics.ONE_MINUTE_IN_MILLIS;

                        // finding closest agent
                        if (currPickupTime < timeToPickup) {
                            timeToPickup = currPickupTime;
                            closestAgent = agent;
                        }
                    }
                }


                // check if agent can be assigned and assign agent and stop checking
                if (closestAgent != null && (timeToPickup + resource.waitTime) <= MLT) {

                    assignAgentToResource(closestAgent, resource, timeToPickup);
                    break;
                }
                // if not assigned, check the next ring of hexagons
                currentRing++;
            }
            // if resource didn't get allocated even after K rounds then add it in waiting
            if (!resource.assigned) {

                // new resource, then add to waiting count
                if (resource.waitTime == 0) Statistics.totalWaitingResources += 1;
                resources.add(resource);
                resource.waitTime += Statistics.incrementalTimeWindow;
                Statistics.accumulatedWaitTime += Statistics.incrementalTimeWindow;
//                System.out.println("wait time: " + r.accumulatedWaitTime);
            }
        }
        printStats();
        return resources;
    }

    private void assignAgentToResource(Agent closestAgent, Resource resource, double timeToPickup) throws IOException {

        closestAgent.status = 1;
        resource.assigned = true;

        String destination_hex_id = Helper.getHexFromGeo(resource.dropLocation.latitude, resource.dropLocation.longitude);

//        double journeyTime = (Math.abs(resource.dropTime.getTime() - resource.pickupTime.getTime()) / Statistics.ONE_MINUTE_IN_MILLIS) + timeToPickup;
        double journeyTime = Graphhopper.time(resource.pickupLocation.latitude, resource.pickupLocation.longitude, resource.dropLocation.latitude, resource.dropLocation.longitude) / Statistics.ONE_MINUTE_IN_MILLIS + timeToPickup;


        closestAgent.setDestination(destination_hex_id, 0, journeyTime, 1);

        // add to assigned list
        Statistics.totalAssignedAgents += 1;

        if (AgentGeneration.freeAgentList.remove(closestAgent.agentId).agentId == closestAgent.agentId) {
//            System.out.println("Removed agent from free list: " + closestAgent.agentId + " : " + closestAgent.status + closestAgent.currentTravelTime);
        } else {
            System.out.println("Something went wrong: " + closestAgent.agentId + " : " + closestAgent.status);
        }

        // remove it from waiting list if it was waiting
        if (resource.waitTime > 0) Statistics.totalWaitingResources -= 1;
    }

    private void dropResource(Resource resource) {
//        System.out.println("Dropped cab: " + resource.hexagonId + " because of accumulatedWaitTime: " + resource.waitTime);
        Statistics.totalDroppedResources += 1;
        Statistics.totalWaitingResources -= 1;
    }

    private void printStats() {

        System.out.println("Total Dropped Resources: " + Statistics.totalDroppedResources);
        System.out.println("Total Assigned Resources: " + Statistics.totalAssignedAgents);
        System.out.println("Total Waiting Resources: " + Statistics.totalWaitingResources);

        double totalResources = Statistics.totalDroppedResources + Statistics.totalAssignedAgents + Statistics.totalWaitingResources;

        System.out.println("Total Invalid Resources: " + Statistics.totalInvalidResources);
//        System.out.println("Total Out of Bounds Resources: " + Statistics.outOfBoundsResources);

        System.out.println("Total Wait Time: " + Statistics.accumulatedWaitTime);
        System.out.println("Total Search Time: " + Statistics.accumulatedSearchTime);

        Statistics.totalFreeAgents = AgentGeneration.freeAgentList.size();

        Statistics.averageWaitTime = (Statistics.accumulatedWaitTime / totalResources);
        Statistics.averageSearchTime = (Statistics.accumulatedSearchTime / Statistics.totalAssignedAgents);
        Statistics.droppedResourcePercentage = (double) Statistics.totalDroppedResources / (Statistics.totalAssignedAgents + Statistics.totalDroppedResources);
        Statistics.droppedResourcePercentageB = (Statistics.totalDroppedResources + Statistics.totalWaitingResources) / totalResources;

        System.out.println("Average Wait Time: " + Statistics.averageWaitTime);
        System.out.println("Average Search Time: " + Statistics.averageSearchTime);
        System.out.println("Dropped Resource Percentage: " + (Statistics.droppedResourcePercentage * 100));
        System.out.println("Dropped Resource Percentage: " + (Statistics.droppedResourcePercentageB * 100) + '\n');

        System.out.println("Number of Free Agents after allocation: " + Statistics.totalFreeAgents + '\n');

        Statistics.averageSearchTimeList.add(Statistics.averageSearchTime);
        Statistics.averageWaitTimeList.add(Statistics.averageWaitTime);
        Statistics.totalFreeAgentsList.add(Statistics.totalFreeAgents);
        Statistics.totalOccupiedAgentsList.add(Main.noOfCabs - Statistics.totalFreeAgents);
        Statistics.expirationPercentageList.add(Statistics.droppedResourcePercentage * 100);
        Statistics.expirationPercentageBList.add(Statistics.droppedResourcePercentageB * 100);
    }
}