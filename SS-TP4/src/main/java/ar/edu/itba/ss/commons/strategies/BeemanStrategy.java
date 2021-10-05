package ar.edu.itba.ss.commons.strategies;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.system1.Config;

public class BeemanStrategy implements UpdateStrategy{

    private final double K;
    private final double gamma;
    private final EulerStrategy euler;

    public BeemanStrategy(Config config){
        this.K = config.getK();
        this.gamma = config.getGamma();
        this.euler = new EulerStrategy(config);
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

        double newPosX = present.getPosX() + (present.getVelX() * deltaT) + ( (2.0/3)*present.getAccX()*Math.pow(deltaT,2) ) - ( (1.0/6)* past.getAccX() * Math.pow(deltaT,2) ) ;
        double newPosY = present.getPosY() + (present.getVelY() * deltaT) + ( (2.0/3)*present.getAccY()*Math.pow(deltaT,2) ) - ( (1.0/6)* past.getAccY() * Math.pow(deltaT,2) ) ;

        double predictedVelX = present.getVelX() + 1.5*present.getAccX()*deltaT -0.5*past.getAccX()*deltaT;
        double predictedVelY = present.getVelY() + 1.5*present.getAccY()*deltaT -0.5*past.getAccY()*deltaT;

        double predictedAcX = (-K*newPosX - gamma*predictedVelX)/mass;
        double predictedAcY = (-K*newPosY - gamma*predictedVelY)/mass;

        Particle future = present.clone();
        double correctedVelX = present.getVelX() + (1.0/3)*predictedAcX*deltaT+(5.0/6)*present.getAccX()*deltaT-1.0/6*past.getAccX()*deltaT;
        double correctedVelY = present.getVelY() + (1.0/3)*predictedAcY*deltaT+(5.0/6)*present.getAccY()*deltaT-1.0/6*past.getAccY()*deltaT;

        future.setVelX(correctedVelX);
        future.setVelY(correctedVelY);
        double correctedAcX = (-K*newPosX - gamma*correctedVelX)/mass;
        double correctedAcY = (-K*newPosY - gamma*correctedVelY)/mass;

        future.setPosX(newPosX);
        future.setPosY(newPosY);

        future.setAccX(correctedAcX);// actualizamos la acc
        future.setAccY(correctedAcY);

        return future;

    }

    @Override
    public String getName() {
        return "beeman";
    }
}
