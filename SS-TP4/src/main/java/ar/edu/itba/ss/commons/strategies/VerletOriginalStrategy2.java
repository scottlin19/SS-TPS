package ar.edu.itba.ss.commons.strategies;
import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.Config;

import java.util.function.Function;

public class VerletOriginalStrategy2 implements UpdateStrategy{


    private final EulerStrategy2 euler;
//    private final Function<Particle, Double> forceX;
//    private final Function<Particle, Double> forceY;

    public VerletOriginalStrategy2(){
        this.euler = new EulerStrategy2();
//        this.forceX = forceX;
//        this.forceY = forceY;
    }

    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        if(present == null){
            throw new NullPointerException();
        }
        if(past == null){
            // Resolver con euler
            past = euler.update(null,present,-deltaT, time);
        }

        double newPosX = 2*present.getPosX() - past.getPosX() + Math.pow(deltaT,2) * present.getAccX();
        double newPosY = 2*present.getPosY() - past.getPosY() + Math.pow(deltaT,2) * present.getAccY();

        double newVelX = (newPosX - past.getPosX())/(2*deltaT);
        double newVelY = (newPosY - past.getPosY())/(2*deltaT);

        present.setVelX(newVelX);
        present.setVelY(newVelY);
        Particle future = present.clone();
        future.setPosX(newPosX);
        future.setPosY(newPosY);

//        future.setAccX((forceX.apply(future))/mass);// actualizamos la fuerza
//        future.setAccY((forceY.apply(future))/mass);

        return future;
    }
}
