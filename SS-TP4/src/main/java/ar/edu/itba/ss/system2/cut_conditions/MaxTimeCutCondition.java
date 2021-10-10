package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public class MaxTimeCutCondition extends CutCondition{

    private double maxTime;
    private double deltaT;
    private double time;
    public MaxTimeCutCondition(double maxTime, double deltaT){
        this.maxTime = maxTime;
        this.deltaT = deltaT;
        this.time = 0;
    }
    @Override
    public boolean cut(Particle spaceship, Particle target) {
        time+=deltaT;
        boolean cut = time > maxTime;
        if(cut){
            System.out.println("MAXIMUM TIME REACHED, TERMINATING SIMULATION");
        }
        return cut;

    }
}
