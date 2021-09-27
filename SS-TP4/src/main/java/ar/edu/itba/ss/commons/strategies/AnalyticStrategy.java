package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SystemFunctions;

public class AnalyticStrategy extends UpdateStrategy{

    public AnalyticStrategy(double deltaT, SystemFunctions systemFunctions) {
        super(deltaT,systemFunctions);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(Particle p1, double t) {
        p1.setPosX(systemFunctions.r(p1, t + deltaT));
        p1.setVelX(systemFunctions.r1(p1, t + deltaT));
    }
}
