import java.util.Date;

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

    static double ONE_MINUTE_IN_MILLIS = 60000; // milliseconds

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
