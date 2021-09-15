package ar.edu.itba.ss.brownian_motion;

import ar.edu.itba.ss.commons.SimulationSnapshot;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.util.List;

public class SimulationResult {

    private final long totalCollisions;
    private final double totalTime;
    private final List<SimulationSnapshot> snapshots;

    public SimulationResult(long totalCollisions, double totalTime, List<SimulationSnapshot> snapshots) {
        this.totalCollisions = totalCollisions;
        this.totalTime = totalTime;
        this.snapshots = snapshots;
    }

    public long getTotalCollisions() {
        return totalCollisions;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }


}
