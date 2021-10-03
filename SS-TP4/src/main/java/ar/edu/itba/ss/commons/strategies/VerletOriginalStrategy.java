package ar.edu.itba.ss.commons.strategies;
import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.Config;

import java.util.function.Function;

public class VerletOriginalStrategy implements UpdateStrategy{


    private final EulerStrategy euler;
    private final double K;
    private final double gamma;

    public VerletOriginalStrategy(Config config){
        this.euler = new EulerStrategy(config);
        this.K = config.getK();
        this.gamma = config.getGamma();
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
        double mass = present.getMass();
        double newPosX = 2*present.getPosX() - past.getPosX() + Math.pow(deltaT,2) * present.getAccX();
        double newPosY = 2*present.getPosY() - past.getPosY() + Math.pow(deltaT,2) * present.getAccY();

        double newVelX = (newPosX - past.getPosX())/(2*deltaT);

        double newVelY = (newPosY - past.getPosY())/(2*deltaT);

        present.setVelX(newVelX);
        present.setVelY(newVelY);
        Particle future = present.clone();

        future.setPosX(newPosX);
        future.setPosY(newPosY);

        future.setAccX((-K * future.getPosX()-gamma * future.getVelX())/mass);// actualizamos la fuerza
        future.setAccY((-K * future.getPosY()-gamma * future.getVelY())/mass);

        return future;
    }
    

}
