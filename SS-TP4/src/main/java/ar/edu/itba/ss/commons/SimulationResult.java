package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationResult {
    private final double                    totalTime;
    private final List<SimulationSnapshot>  snapshots;
    private final String                    method;

    public SimulationResult(double totalTime, List<SimulationSnapshot> snapshots, String method) {
        this.totalTime = totalTime;
        this.snapshots = snapshots;
        this.method = method;
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
