import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Statistics {

    static int totalAssignedAgents = 0;

    static int totalDroppedResources = 0;
    static int totalWaitingResources = 0;
    static int totalInvalidResources = 0;
    static int outOfBoundsResources = 0;

    static double accumulatedWaitTime = 0; // for all resources
    static double accumulatedSearchTime = 0; // for all agents

    static double incrementalTimeWindow = 0; // in minutes
    static Date currTime = null;

    static double ONE_MINUTE_IN_MILLIS = 60000.0; // milliseconds

    static double averageSearchTime = 0;
    static double averageWaitTime = 0;
    static double droppedResourcePercentage = 0;
    static double droppedResourcePercentageB = 0;
    static double totalFreeAgents = 0;

    static List<Double> averageSearchTimeList = new ArrayList<>();
    static List<Double> averageWaitTimeList = new ArrayList<>();
    static List<Double> totalOccupiedAgentsList = new ArrayList<>();
    static List<Double> totalFreeAgentsList = new ArrayList<>();
    static List<Double> expirationPercentageList = new ArrayList<>();
    static List<Double> expirationPercentageBList = new ArrayList<>();

//    Statistics(double incrementalTimeWindow, Date currTime) {
//
//        accumulatedSearchTime = 0;
//        totalDroppedResources = 0;
//        accumulatedWaitTime = 0;
//        this.incrementalTimeWindow = incrementalTimeWindow;
//        totalAssignedAgents = 0;
//        totalWaitingResources = 0;
//        this.currTime = currTime;
//        totalInvalidResources = 0;
//    }
}
