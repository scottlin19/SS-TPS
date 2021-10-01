package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.sistem1.Config;


public class EulerStrategy implements UpdateStrategy{
    private final double K;
    private final double gamma;

    public EulerStrategy(Config config){
        this.K = config.getK();
        this.gamma = config.getGamma();
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

        future.setAccX((-K * future.getPosX()-gamma * future.getVelX())/mass);
        future.setAccY((-K * future.getPosY()-gamma * future.getVelY())/mass);

//        System.out.println("EULER FUTURE: "+future);
        return future;
    }
}
