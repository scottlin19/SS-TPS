package ar.edu.itba.ss.commons.strategies;
import ar.edu.itba.ss.commons.Particle;

public class VerletOriginalStrategy implements UpdateStrategy{


    private final EulerStrategy euler;

    public VerletOriginalStrategy(){
        euler = new EulerStrategy();
    }

    @Override
    public Particle update(Particle past, Particle present, double deltaT, double time) {
        if(present == null){
            throw new NullPointerException();
        }
        if(past == null){
            // Resolver con euler
            past = euler.update(null,present,-deltaT, time);
            System.out.println("With Euler past: " + past);
        }
        Particle future = present.clone();

        double newPosX = 2*present.getPosX() - past.getPosX() + Math.pow(deltaT,2) * present.getForceX()/ present.getMass(); // aceleración constante?
        double newPosY = 2*present.getPosY() - past.getPosY() + Math.pow(deltaT,2) * present.getForceY()/ present.getMass();

        double newVelX = (newPosX - past.getPosX())/(2*deltaT);
        double newVelY = (newPosY - past.getPosY())/(2*deltaT);

        present.setVelX(newVelX);
        present.setVelY(newVelY);

        future.setPosX(newPosX);
        future.setPosY(newPosY);
        return future;
    }
    

}
