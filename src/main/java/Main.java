import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Main {

    static Map<String, Hexagon> hexagonMap;
    static Map<Integer, Agent> agentList;
    static double noOfCabs = 5000.0;

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
        Queue<Resource> resources = resourceAddition.readResourcesFromCSV("src/main/java/data/resource-data.csv");

//        HexagonGeneration loadHexagonData = new HexagonGeneration();
        hexagonMap = HexagonGeneration.readHexagonsFromCSV("src/main/java/data/hexagon-data.csv");

        // Initialize cabs & obtain list of cabs
        AgentGeneration agentGeneration = new AgentGeneration(noOfCabs, startTime);

        agentList = agentGeneration.generateAgents(hexagonMap);

//        System.exit(0);

        Statistics.incrementalTimeWindow = incrementalTimeWindow;
        Statistics.currTime = startTime;

        Queue<Resource> curr_res = new LinkedList<>();
        AllocationFramework allocator = new AllocationFramework();
        SearchAlgorithm searchAlgorithm = new SearchAlgorithm();

        Date s_start = new Date();
        System.out.println("SearchAlgorithm Start Time: " + s_start);
        System.out.println(Statistics.currTime + " " + endTime);

        while (Statistics.currTime.compareTo(endTime) < 0) {

            System.out.println(resources.peek().pickupTime + " " + Statistics.currTime);

//            System.out.println("time comparision: " + resources.peek().pickupTime + " " + Statistics.currTime + " " + resources.peek().pickupTime.compareTo(Statistics.currTime));
            while (!resources.isEmpty() && resources.peek().pickupTime.compareTo(Statistics.currTime) <= 0) {
//                System.out.println("time comparision: " + resources.peek().pickupTime + " " + Statistics.currTime + " " + resources.peek().pickupTime.compareTo(Statistics.currTime));
                curr_res.add(resources.poll());
            }

            System.out.println("No of resources:" + curr_res.size());

            System.out.println("Number of Free Agents before allocation: " + AgentGeneration.freeAgentList.size() + '\n');
            curr_res = allocator.allocate(curr_res, hexagonMap, MLT);

            searchAlgorithm.provideSearchDirections(agentList, hexagonMap);
            System.out.println("Number of Free Agents after directions: " + AgentGeneration.freeAgentList.size() + '\n');

            Statistics.currTime = new Date(Statistics.currTime.getTime() + (int) (Statistics.incrementalTimeWindow * Statistics.ONE_MINUTE_IN_MILLIS));
        }

        Date end_time = new Date();
        System.out.println("SearchAlgorithm Start Time: " + end_time);

        long diff = (end_time.getTime() - s_start.getTime());
        System.out.println("SearchAlgorithm Run Time: " + (diff / Statistics.ONE_MINUTE_IN_MILLIS) + '\n');

        System.out.println("Search_Time_List: " + Statistics.averageSearchTimeList + '\n');
        System.out.println("Wait_Time_List: " + Statistics.averageWaitTimeList + '\n');
        System.out.println("Free_Agents_List: " + Statistics.totalFreeAgentsList + '\n');
        System.out.println("Occupied_Agents_List: " + Statistics.totalOccupiedAgentsList + '\n');
        System.out.println("Expiration_List: " + Statistics.expirationPercentageList + '\n');
        System.out.println("Expiration_B_List: " + Statistics.expirationPercentageBList + '\n');
    }
}


