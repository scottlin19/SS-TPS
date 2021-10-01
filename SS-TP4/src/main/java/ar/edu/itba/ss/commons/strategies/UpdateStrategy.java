package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SystemFunctions;

public interface UpdateStrategy {

    Particle update(Particle past, Particle present, double deltaT, double time);

}
