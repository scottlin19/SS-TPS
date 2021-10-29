package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationSnapshot {

    private final List<Pedestrian>    pedestrians;
    private final double              time;

    public SimulationSnapshot(List<Pedestrian> pedestrians, double time){
        this.pedestrians = pedestrians;
        this.time = time;
    }

    public List<Pedestrian> getPedestrians() {
        return pedestrians;
    }

    public double getTime() {
        return time;
    }
}
