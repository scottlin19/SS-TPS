package ar.edu.itba.ss.system2;

import ar.edu.itba.ss.commons.*;
import ar.edu.itba.ss.system2.cut_conditions.LandedOnTargetCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MaxTimeCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MissedTargetCutCondition;
import java.util.List;

public class MarsMission extends AbstractMission{

    public MarsMission(SpaceMissionConfig config){

        super(config);
        setAcc(this.mars, List.of(this.earth, this.sun));
        setAcc(this.earth, List.of(this.mars, this.sun));

        takenOff = false;
        this.missedTargetCC = new MissedTargetCutCondition(mars);
        this.maxTimeCC = new MaxTimeCutCondition(config.getMaxTime(),config.getDeltaT());

        int DEIMOS = 23460;
        this.landedOnTargetCC = new LandedOnTargetCutCondition(DEIMOS);
    }

    @Override
    public SpaceMissionResult simulate(double deltaT, double takeOffTime, double initialVelocity){
        System.out.println("Starting mars mission with takeOff Time: "+takeOffTime);
        double currentTime = 0;
        Particle pastMars = null;
        Particle pastEarth = null;
        Particle pastSpaceship = null;
        Particle futureMars;
        Particle futureEarth;
        Particle futureSpaceship;
        int step = config.getStep();
        int i = 0;

        while(!landedOnTargetCC.cut(spaceship,mars) && !missedTargetCC.cut(spaceship,mars) && !maxTimeCC.cut(spaceship,mars) ){
            if (!takenOff && currentTime >= takeOffTime){
                System.out.printf("Taking off... (%.0fs)\n",currentTime);
                createSpaceship(initialVelocity, List.of(earth, sun, mars));
                takenOff = true;
                targetMinDistance = Double.min(targetMinDistance,Particle.dist(mars,spaceship));
            }

            futureEarth = updateStrategy.update(pastEarth, earth, deltaT, currentTime);
            futureMars = updateStrategy.update(pastMars, mars, deltaT, currentTime);
            pastEarth = earth;
            pastMars = mars;
            earth = futureEarth;
            mars = futureMars;
            setAcc(earth, List.of(mars, this.sun));
            setAcc(mars, List.of(earth, this.sun));
//            setAcc(futureEarth, List.of(futureMars, this.sun));
//            setAcc(futureMars, List.of(futureEarth, this.sun));
//            pastEarth = earth;
//            pastMars = mars;

//            earth = futureEarth;
//            mars = futureMars;
            if(takenOff){
                futureSpaceship = updateStrategy.update(pastSpaceship, spaceship, deltaT, currentTime);
                setAcc(futureSpaceship, List.of(futureEarth,futureMars,this.sun));
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
                targetMinDistance = Double.min(targetMinDistance,Particle.dist(spaceship,mars));
            }

            if(i % step == 0){
                if (takenOff) {
                    snapshots.add(new SimulationSnapshot(List.of(spaceship, mars, earth, sun), currentTime));
                }else{
                    snapshots.add(new SimulationSnapshot(List.of(mars, earth, sun), currentTime));
                }
            }
            currentTime += deltaT;
            i++;
        }

        if (takenOff) {
            snapshots.add(new SimulationSnapshot(List.of(spaceship, mars, earth, sun), currentTime));
        }else{
            snapshots.add(new SimulationSnapshot(List.of(mars, earth, sun), currentTime));
        }

        System.out.printf("Simulation finished at time %.0fs with minimum distance to mars = %fkm\n", currentTime, targetMinDistance - mars.getRadius());
        return new SpaceMissionResult(config.getTarget(), currentTime,takeOffTime,targetMinDistance - mars.getRadius(),isSuccessful(mars),initialVelocity,snapshots);

    }

    @Override
    public String getName() {
        return "MarsMission";
    }


}
