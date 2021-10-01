package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationResult {
    private final double                    totalTime;
    private final String                    method;
    private final List<SimulationSnapshot>  snapshots;

    public SimulationResult(double totalTime, List<SimulationSnapshot> snapshots, String method) {
        this.totalTime = totalTime;
        this.method = method;
        this.snapshots = snapshots;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }


    public String getMethod() {
        return method;
    }
}
