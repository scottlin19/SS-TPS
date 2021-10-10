package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

public class LandedOnMarsCutCondition extends CutCondition{

    private final int LIMIT;

    public LandedOnMarsCutCondition(int limit){
        this.LIMIT = limit;
    }
    @Override
    public boolean cut(Particle spaceship, Particle mars) {
        if(spaceship == null){
            return false;
        }
        //System.out.println("spceship: "+spaceship + "\nmars: "+mars);
       // System.out.println("LANDED ON MARS CUT: "+Math.sqrt(Math.pow(mars.getPosX()-spaceship.getPosX(),2)  + Math.pow(mars.getPosY() - spaceship.getPosY(),2)) + "mars radius: "+mars.getRadius());
        boolean cut = Particle.dist(spaceship,mars) <= mars.getRadius() + LIMIT;
        if(cut){
            System.out.println("SPACESHIP LANDED ON MARS!");
        }
        return cut;
    }




}
