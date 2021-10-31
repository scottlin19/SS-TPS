package ar.edu.itba.ss.commons;

import java.util.List;
import java.util.stream.Collectors;

public class SimulationSnapshot {

    private final List<PedestrianDTO>       pedestrians;
    private final List<PedestrianDTO>       exited;
    private final double                    time;

    public SimulationSnapshot(List<Pedestrian> pedestrians, List<Pedestrian> exited,double time){
        this.pedestrians = pedestrians.stream().map(PedestrianDTO::fromPedestrian).collect(Collectors.toList());
        this.exited = exited.stream().map(PedestrianDTO::fromPedestrian).collect(Collectors.toList());
        this.time = time;
    }

    public List<PedestrianDTO> getPedestrians() {
        return pedestrians;
    }

    public double getTime() {
        return time;
    }

    public List<PedestrianDTO> getExited() {
        return exited;
    }
}
