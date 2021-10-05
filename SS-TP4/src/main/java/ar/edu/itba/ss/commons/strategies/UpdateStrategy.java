package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;

public interface UpdateStrategy {

    Particle update(Particle past, Particle present, double deltaT, double time);
    String getName();

}
