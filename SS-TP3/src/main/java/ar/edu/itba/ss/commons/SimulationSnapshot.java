package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.brownian_motion.Event;

import java.util.List;
import java.util.stream.Collectors;

public class SimulationSnapshot {
    private final List<Particle> particlesData;
    private final Event event;

    public SimulationSnapshot(List<Particle> particles, Event event){
        this.particlesData = particles.stream().map(p -> new Particle(p.getId(),p.getPosX(),p.getPosY(),p.getRadius(), p.getMass(),p.getVelX(),p.getVelY())).collect(Collectors.toList());
        this.event = event;
    }

    public List<Particle> getParticles() {
        return particlesData;
    }

    public Event getEvent() {
        return event;
    }
}
