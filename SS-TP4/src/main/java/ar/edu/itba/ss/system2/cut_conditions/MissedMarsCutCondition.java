package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public class MissedMarsCutCondition extends CutCondition{

    private final double marsRadius;
    private final double marsOrbitRadius;
    public MissedMarsCutCondition(Particle mars){
        this.marsRadius = mars.getRadius();
        this.marsOrbitRadius = Math.sqrt(Math.pow(mars.getPosX(),2) + Math.pow(mars.getPosY(),2));
    }

    @Override
    public boolean cut(Particle spaceship,Particle mars) {
        if(spaceship == null){
            return true;
        }
        boolean cut = Math.sqrt(Math.pow(spaceship.getPosX(),2) + Math.pow(spaceship.getPosY(),2)) > marsOrbitRadius+marsRadius;
        if(cut){
            System.out.println("SPACESHIP MISSED MARS, TERMINATING SIMULATION");
        }
        return cut;

    }


}
