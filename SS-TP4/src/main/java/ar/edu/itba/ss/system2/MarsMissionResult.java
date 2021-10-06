package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

import java.util.List;


public class MarsMissionResult extends SimulationResult {
    private final double marsDistance;
    private final boolean isSuccessful;
    private final double takeOffTime;
    private final double takeOffSpeed;

    public MarsMissionResult(double totalTime,double takeOffTime,double marsDistance,boolean isSuccessful,double takeOffSpeed, List<SimulationSnapshot> snapshots) {
        super(totalTime, snapshots, "verlet");
        this.marsDistance = marsDistance;
        this.isSuccessful = isSuccessful;
        this.takeOffTime = takeOffTime;
        this.takeOffSpeed  = takeOffSpeed;
    }

    public double getTakeOffSpeed() {
        return takeOffSpeed;
    }

    public double getTakeOffTime() {
        return takeOffTime;
    }

    public double getMarsDistance() {
        return marsDistance;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
