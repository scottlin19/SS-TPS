package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

import java.util.List;


public class SpaceMissionResult extends SimulationResult {
    private final String target;
    private final double targetDistance;
    private final boolean isSuccessful;
    private final double takeOffTime;
    private final double takeOffSpeed;

    public SpaceMissionResult(String target, double totalTime, double takeOffTime, double targetDistance, boolean isSuccessful, double takeOffSpeed, List<SimulationSnapshot> snapshots) {
        super(totalTime, snapshots, "verlet");
        this.target = target;
        this.targetDistance = targetDistance;
        this.isSuccessful = isSuccessful;
        this.takeOffTime = takeOffTime;
        this.takeOffSpeed  = takeOffSpeed;
    }

    public String getTarget() {
        return target;
    }

    public double getTakeOffSpeed() {
        return takeOffSpeed;
    }

    public double getTakeOffTime() {
        return takeOffTime;
    }

    public double getTargetDistance() {
        return targetDistance;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
