package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;

import java.util.List;


public class MarsMissionResult extends SimulationResult {
    private double marsDistance;
    private boolean isSuccessful;
    private double takeOffTime;

    public MarsMissionResult(double totalTime,double takeOffTime,double marsDistance,boolean isSuccessful, List<SimulationSnapshot> snapshots) {
        super(totalTime, snapshots, "verlet");
        this.marsDistance = marsDistance;
        this.isSuccessful = isSuccessful;
        this.takeOffTime = takeOffTime;
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
