package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.Event;
import ar.edu.itba.ss.grid.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimulationSnapshot {
    private final List<Particle> particlesData;
    private final Event event;

    public SimulationSnapshot(List<Particle> particles, Event event){
        this.particlesData = new ArrayList<>(particles);
        this.event = event;
    }

    public List<Particle> getParticles() {
        return particlesData;
    }

    public Event getEvent() {
        return event;
    }
}
