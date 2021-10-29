package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationResult {
    private final double                    totalTime;
    private final List<SimulationSnapshot>  snapshots;

    public SimulationResult(List<SimulationSnapshot> snapshots ,double totalTime) {
        this.snapshots = snapshots;
        this.totalTime = totalTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }
}
