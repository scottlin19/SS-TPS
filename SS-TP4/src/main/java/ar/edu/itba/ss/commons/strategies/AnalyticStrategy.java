package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.SystemFunctions;

public class AnalyticStrategy implements UpdateStrategy{

    private SystemFunctions functions;

    public AnalyticStrategy(SystemFunctions functions) {
        this.functions = functions;

    }

    public AnalyticStrategy(){

    }

    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        Particle future = present.clone();
        future.setPosX(functions.rx(present, time + deltaT));
        future.setVelX(functions.r1x(present, time + deltaT));
        future.setAccX(functions.r2x(present, time + deltaT));
        return future;
    }

    public AnalyticStrategy withFunctions(SystemFunctions functions){
        this.functions = functions;
        return this;
    }
}
