package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public class EnteredOrbitCutCondition  extends CutCondition{

    private final int LIMIT;
    private boolean enteredOrbit;
    private State state;

    public EnteredOrbitCutCondition(int limit){
        this.LIMIT = limit;
        this.enteredOrbit = false;
        this.state  = State.MISS;
    }


    @Override
    public boolean cut(Particle spaceship, Particle target) {
        if(spaceship == null){
            return false;
        }
        double dist = Particle.dist(spaceship,target);
        boolean inOrbit =  dist <= target.getRadius() + LIMIT;
        // Se escapó de la orbita
        if(inOrbit){
            if(state == State.MISS){
                state = State.IN_ORBIT;
            }
            if(!enteredOrbit){
                enteredOrbit = true;
            }
            //Choco con target
            if(dist <= target.getRadius()){
                state = State.LANDED;
                return true;
            }
            return false;

        }else{
            if(enteredOrbit){
                enteredOrbit = false;

                return true;
            }
            return false;
        }
    }

    public State getState() {
        return state;
    }
}
