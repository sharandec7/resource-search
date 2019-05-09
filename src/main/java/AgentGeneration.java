import java.text.ParseException;
import java.util.*;

class AgentGeneration {

    private double noOfAgents;
    private Date start_time;

    private Utilities helper = new Utilities();

    static Map<Integer, Agent> freeAgentList;

    AgentGeneration(double noOfAgents, Date start_time) {

        this.noOfAgents = noOfAgents;
        this.start_time = start_time;
    }

    Map<Integer, Agent> generateAgents(Map<String, Hexagon> hexagonMap) throws ParseException {

        Map<Integer, Agent> agentList = new HashMap<>((int) noOfAgents);
        freeAgentList = new HashMap<>((int) noOfAgents);


        List<String> hexagonList = new ArrayList<>(hexagonMap.keySet());

        Agent agent;
        Hexagon currentHexagon;

        int countOfAgents = 1, index, totalHexagons = hexagonMap.size();

        String currentTimeInterval = helper.getTimeStamp(start_time);

        while (countOfAgents < noOfAgents) {

            index = (countOfAgents - 1) % totalHexagons;
            String currentHexId = hexagonList.get(index);

            currentHexagon = hexagonMap.get(currentHexId);

            agent = new Agent(countOfAgents - 1, currentHexId, currentTimeInterval, currentHexagon.center);
            agentList.put(countOfAgents - 1, agent);
            freeAgentList.put(countOfAgents - 1, agent);
            currentHexagon.agentsPresent.add(agent);

//            System.out.println( "count: "+count+" i: "+i+" hexagon: " + hexagon_map.get(hexagon_id) + " assigned agent: " + agentList.get(count - 1));
            countOfAgents++;
        }

        System.out.println("No of Hexagons: " + hexagonMap.size() + " No of Agents: " + agentList.size());
        return agentList;
    }
}
