package ar.edu.itba.ss.system2.cut_conditions;

import ar.edu.itba.ss.commons.Particle;

import java.awt.*;

public class LandedOnMarsCutCondition extends CutCondition{

    @Override
    public boolean cut(Particle spaceship, Particle mars) {
        if(spaceship == null){
            return true;
        }
       // System.out.println("Landed on mars: "+(Math.sqrt(Math.pow(mars.getPosX()-spaceship.getPosX(),2)  + Math.pow(mars.getPosY() - spaceship.getPosY(),2)) < mars.getRadius()));
        return Math.sqrt(Math.pow(mars.getPosX()-spaceship.getPosX(),2)  + Math.pow(mars.getPosY() - spaceship.getPosY(),2)) >= mars.getRadius();
    }




}
