package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.system2.cut_conditions.CutCondition;

import java.util.List;
import java.util.Objects;


public class SpaceMissionResult extends SimulationResult {
    private final String target;
    private final double targetDistance;
    private final String state;
    private final double takeOffTime;
    private final double takeOffSpeed;

    public SpaceMissionResult(String target, double totalTime, double takeOffTime, double targetDistance, String state, double takeOffSpeed, List<SimulationSnapshot> snapshots) {
        super(totalTime, snapshots, "verlet");
        this.target = target;
        this.targetDistance = targetDistance;
        this.state = state;
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

    public String getState() {
        return state;
    }

    public boolean isSuccessful(){
        return !Objects.equals(state, CutCondition.State.MISS.toString());
    }
}
