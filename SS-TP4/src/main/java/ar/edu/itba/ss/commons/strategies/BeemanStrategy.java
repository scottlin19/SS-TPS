package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.sistem1.Config;

public class BeemanStrategy implements UpdateStrategy{

    private final double K;
    private final double gamma;

    public BeemanStrategy(Config config){
        this.K = config.getK();
        this.gamma = config.getGamma();
    }

    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {


        return null;
    }
}
