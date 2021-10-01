package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationResult {
    private final double                    totalTime;
    private final List<SimulationSnapshot>  snapshots;

    public SimulationResult(double totalTime, List<SimulationSnapshot> snapshots) {
        this.totalTime = totalTime;
        this.snapshots = snapshots;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }


}
