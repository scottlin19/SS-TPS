package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SystemFunctions;

public abstract class UpdateStrategy {

    protected double deltaT;
    protected final SystemFunctions systemFunctions;

    public UpdateStrategy(double deltaT, SystemFunctions systemFunctions){
        this.deltaT = deltaT;
        this.systemFunctions = systemFunctions;
    }

    public void setDeltaT(double deltaT){
        this.deltaT =  deltaT;
    }

    public abstract void init();

    public abstract void update(Particle p1, double t);
}
