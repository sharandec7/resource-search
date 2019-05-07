import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Main {

    static Map<String, Hexagon> hexagonMap;

    public static void main(String[] args) throws ParseException, IOException {

        Date startTime, endTime;
        Calendar cal = Calendar.getInstance();
        double MLT = 10.0;
        double incrementalTimeWindow = 0.5; // minutes

        cal.set(2016, Calendar.MAY, 4, 8, 00, 00);
        startTime = cal.getTime();
        cal.set(2016, Calendar.MAY, 4, 9, 00, 00);
        endTime = cal.getTime();

        // obtain Resource list - given CSV of rides between input startTime and endTime,
        ResourceAddition resourceAddition = new ResourceAddition();
        Queue<Resource> resources = resourceAddition.readResourcesFromCSV("src/main/java/data/sample_data-full.csv");

//        PreProcess loadHexagonData = new PreProcess();
        hexagonMap = PreProcess.readHexagonsFromCSV("src/main/java/data/probability-updated.csv");

        // Initialize cabs & obtain list of cabs
        int noOfCabs = 1000;
        AgentGeneration agentGeneration = new AgentGeneration(noOfCabs, startTime);

        List<Agent> agentList = agentGeneration.generateAgents(hexagonMap);

//        System.exit(0);

        Statistics.incrementalTimeWindow = incrementalTimeWindow;
        Statistics.currTime = startTime;

        Queue<Resource> curr_res = new LinkedList<>();
        Allocation allocator = new Allocation();
        SearchAlgorithm searchAlgorithm = new SearchAlgorithm();

        Date s_start = new Date();
        System.out.println("SearchAlgorithm Start Time: " + s_start);
        System.out.println(Statistics.currTime + " " + endTime);

        while (Statistics.currTime.compareTo(endTime) < 0) {

            searchAlgorithm.provideSearchDirections(agentList, hexagonMap);

            System.out.println(resources.peek().pickup_time + " " + Statistics.currTime);

//            System.out.println("time comparision: " + resources.peek().pickup_time + " " + Statistics.currTime + " " + resources.peek().pickup_time.compareTo(Statistics.currTime));
            while (!resources.isEmpty() && resources.peek().pickup_time.compareTo(Statistics.currTime) <= 0) {
//                System.out.println("time comparision: " + resources.peek().pickup_time + " " + Statistics.currTime + " " + resources.peek().pickup_time.compareTo(Statistics.currTime));
                curr_res.add(resources.poll());
            }

            System.out.println("No of resources:" + curr_res.size());

            curr_res = allocator.allocate(curr_res, hexagonMap, MLT);

            Statistics.currTime = new Date(Statistics.currTime.getTime() + (int) (Statistics.incrementalTimeWindow * Statistics.ONE_MINUTE_IN_MILLIS));
        }

        Date e_start = new Date();
        System.out.println("SearchAlgorithm Start Time: " + e_start);
        long diff = (e_start.getTime() - s_start.getTime());
        System.out.println("SearchAlgorithm Run Time: " + (diff / Statistics.ONE_MINUTE_IN_MILLIS));
    }

    public void updateCurrentHex(Agent agent, double latitude, double longitude) throws IOException {

        agent.currentHexId = Utilities.getHexFromGeo(latitude, longitude);
    }
}


