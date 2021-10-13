package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public abstract class CutCondition {

    public abstract boolean cut(Particle spaceship,Particle target);
    public abstract State getState();
    public enum State{
        LANDED, IN_ORBIT,MISS
    }

}
