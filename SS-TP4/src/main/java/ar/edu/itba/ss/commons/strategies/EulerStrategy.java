package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;


public class EulerStrategy implements UpdateStrategy{

    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        if(present == null){
            throw new NullPointerException();
        }
        System.out.println("EULER PRESENT: "+present );
        System.out.println("force x : "  +present.getForceX());
        Particle future = present.clone();
        double mass = present.getMass();
        future.setVelX(present.getVelX() + deltaT * present.getForceX()/mass);
        future.setVelY(present.getVelY() + deltaT * present.getForceY()/mass);
        future.setPosX(present.getPosX() + deltaT * future.getVelX() + Math.pow(deltaT,2) * present.getForceX()/ (2*mass));
        future.setPosY(present.getPosY() + deltaT * future.getVelY() + Math.pow(deltaT,2) * present.getForceY()/ (2*mass));
        System.out.println("EULER FUTURE: "+future);
        return future;
    }
}
