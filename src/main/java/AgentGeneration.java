import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

class AgentGeneration {

    private int noOfAgents;
    private Date start_time;

    private Utilities helper = new Utilities();

    AgentGeneration(int noOfAgents, Date start_time) {
        this.noOfAgents = noOfAgents;
        this.start_time = start_time;
    }

    List<Agent> generateAgents(Map<String, Hexagon> hexagonMap) throws ParseException {

        List<Agent> agentList = new ArrayList<>();

        List<String> hexagonList = new ArrayList<>(hexagonMap.keySet());

        Agent agent;
        Hexagon currentHexagon;

        int countOfAgents = 1, index, totalHexagons = hexagonMap.size();

        String currentTimeInterval = helper.getTimeStamp(start_time);

        while (countOfAgents < noOfAgents) {

            index = (countOfAgents - 1) % totalHexagons;
            String currentHexId = hexagonList.get(index);

            currentHexagon = hexagonMap.get(currentHexId);

            agent = new Agent(countOfAgents-1, currentHexId, currentTimeInterval, currentHexagon.center);
            agentList.add(agent);
            currentHexagon.agentsPresent.add(agent);

//            System.out.println( "count: "+count+" i: "+i+" hexagon: " + hexagon_map.get(hexagon_id) + " assigned agent: " + agentList.get(count - 1));
            countOfAgents++;
        }

        System.out.println("No of Hexagons: " + hexagonMap.size() + " No of Agents: " + agentList.size());
        return agentList;
    }
}
