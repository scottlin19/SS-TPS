package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.cim.Particle;

import java.util.ArrayList;
import java.util.List;

public class GridSnapshot {
    private final List<ParticleDTO> particlesData;
    private final double VA;

    public GridSnapshot(List<Particle> particles){
        particlesData = new ArrayList<>();
        double counterX = 0;
        double counterY = 0;
        for(Particle particle : particles){
            particlesData.add(ParticleDTO.fromParticle(particle));
            counterX += Math.cos(particle.getDirection());
            counterY += Math.sin(particle.getDirection());
        }
        this.VA = (1.0/ particles.size()) * Math.sqrt(Math.pow(counterX,2) + Math.pow(counterY,2));
    }

    public List<ParticleDTO> getParticlesData() {
        return particlesData;
    }

    public double getVA() {
        return VA;
    }
}
