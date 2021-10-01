package ar.edu.itba.ss.commons;

import java.util.List;

public class SimulationSnapshot {

    private final List<Particle>      particles;
    private final double              time;

    public SimulationSnapshot(List<Particle> particles, double time){
        this.particles = particles;
        this.time = time;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getTime() {
        return time;
    }
}
