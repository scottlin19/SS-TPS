package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SystemFunctions;

public class EulerStrategy extends UpdateStrategy{


    public EulerStrategy(double deltaT, SystemFunctions systemFunctions) {
        super(deltaT,systemFunctions);
    }

    @Override
    public void init() {

    }

    @Override
    public void update(Particle p,double t) {
        double F =  deltaT/p.getMass() *systemFunctions.f(p,t);
        double V = p.getVel() + F;
        double R  = p.getpo
    }
}
