package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationResult {
    private final double                    totalTime;
    private final List<SimulationSnapshot>  snapshots;
    private final List<Wall>             walls;

    public SimulationResult(List<SimulationSnapshot> snapshots ,List<Wall> walls,double totalTime) {
        this.snapshots = snapshots;
        this.totalTime = totalTime;
        this.walls = walls;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }

    public List<Wall> getWalls(){
        return this.walls;
    }
}
