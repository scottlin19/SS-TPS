package ar.edu.itba.ss.commons;

import ar.edu.itba.ss.cim.Particle;

import java.util.List;
import java.util.stream.Collectors;

public class GridSnapshot {
    private final List<ParticleDTO> particlesData;

    public GridSnapshot(List<Particle> particles){
        this.particlesData = particles.stream().map(ParticleDTO::fromParticle).collect(Collectors.toList());
    }

    public List<ParticleDTO> getParticlesData() {
        return particlesData;
    }
}
