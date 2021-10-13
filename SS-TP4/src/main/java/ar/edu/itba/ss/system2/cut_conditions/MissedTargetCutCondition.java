package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public class MissedTargetCutCondition extends CutCondition{

    private final double targetRadius;
    private final double targetOrbitRadius;

    public MissedTargetCutCondition(Particle target){
        this.targetRadius = target.getRadius();
        this.targetOrbitRadius = Math.sqrt(Math.pow(target.getPosX(),2) + Math.pow(target.getPosY(),2));
    }

    @Override
    public boolean cut(Particle spaceship,Particle target) {
        if(spaceship == null){
            return false;
        }
        boolean cut = Math.sqrt(Math.pow(spaceship.getPosX(),2) + Math.pow(spaceship.getPosY(),2)) > targetOrbitRadius + targetRadius;
        if(cut){
            System.out.println("SPACESHIP MISSED TARGET, TERMINATING SIMULATION");
        }
        return cut;

    }

    @Override
    public State getState() {
        return State.MISS;
    }




}
