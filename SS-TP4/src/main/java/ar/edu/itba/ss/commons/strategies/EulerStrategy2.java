package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.Config;

import java.util.function.Function;


public class EulerStrategy2 implements UpdateStrategy{

//    private Function<Particle, Double> forceX;
//    private Function<Particle, Double> forceY;

    public EulerStrategy2(){
//        this.forceX = forceX;
//        this.forceY = forceY;
    }


    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        if(present == null){
            throw new NullPointerException();
        }

        Particle future = present.clone();
        double mass = present.getMass();
        future.setVelX(present.getVelX() + deltaT * present.getAccX());
        future.setVelY(present.getVelY() + deltaT * present.getAccY());
        future.setPosX(present.getPosX() + deltaT * future.getVelX() + Math.pow(deltaT,2) * present.getAccX()/ 2);
        future.setPosY(present.getPosY() + deltaT * future.getVelY() + Math.pow(deltaT,2) * present.getAccY()/ 2);

//        future.setAccX((forceX.apply(future))/mass);
//        future.setAccY((forceY.apply(future))/mass);

//        System.out.println("EULER FUTURE: "+future);
        return future;
    }
}
