package ar.edu.itba.ss.commons;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private final Set<Particle> particles;


    Cell(){
        this.particles = new HashSet<>();
    }

    public Set<Particle> getParticles(){
        return particles;
    }

    public boolean hasParticles(){
        return !this.particles.isEmpty();
    }

    @Override
    public String toString(){
        return particles.toString();
    }
}
