package ar.edu.itba.ss.system2;

import java.util.List;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.system2.cut_conditions.LandedOnTargetCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MaxTimeCutCondition;
import ar.edu.itba.ss.system2.cut_conditions.MissedTargetCutCondition;

public class JupiterMission extends AbstractMission{

    private Particle jupiter;

    public JupiterMission(SpaceMissionConfig config){
        super(config);
        this.jupiter = new Particle(JUPITER_ID,6.500280253784848e8,-3.745881860038198e8,71492,1.89818722e27,6.373758360629619,1.194722075367566e1,0,0, new Particle.Color(0,255,0));
        setAcc(jupiter, List.of(earth, mars, sun));
        this.missedTargetCC = new MissedTargetCutCondition(jupiter);
        int IO = 421600;
        this.landedOnTargetCC = new LandedOnTargetCutCondition(IO);
    }

    @Override
    public SpaceMissionResult simulate(double deltaT, double takeOffTime, double initialVelocity) {
        System.out.println("Starting mars mission with takeOff Time: "+takeOffTime);
        double currentTime = 0;
        Particle pastMars = null;
        Particle pastEarth = null;
        Particle pastSpaceship = null;
        Particle pastJupiter = null;
        Particle futureJupiter;
        Particle futureMars;
        Particle futureEarth;
        Particle futureSpaceship;
        int step = config.getStep();
        int i = 0;

        while(!landedOnTargetCC.cut(spaceship,jupiter) && !missedTargetCC.cut(spaceship,jupiter) && !maxTimeCC.cut(spaceship,jupiter) ){
            if (!takenOff && currentTime >= takeOffTime){
                System.out.printf("Taking off... (%.0fs)\n",currentTime);
                createSpaceship(initialVelocity, List.of(earth, sun, mars));
                takenOff = true;
                targetMinDistance = Double.min(targetMinDistance,Particle.dist(mars,spaceship));
            }

            futureEarth = updateStrategy.update(pastEarth, earth, deltaT, currentTime);
            futureMars = updateStrategy.update(pastMars, mars, deltaT, currentTime);
            futureJupiter = updateStrategy.update(pastJupiter, jupiter, deltaT, currentTime);
            pastEarth = earth;
            pastMars = mars;
            pastJupiter = jupiter;
            earth = futureEarth;
            mars = futureMars;
            jupiter = futureJupiter;
            setAcc(earth, List.of(mars, this.sun, jupiter));
            setAcc(mars, List.of(earth, this.sun, jupiter));
            setAcc(jupiter, List.of(earth, this.sun, mars));
//            setAcc(futureEarth, List.of(futureMars, this.sun));
//            setAcc(futureMars, List.of(futureEarth, this.sun));
//            pastEarth = earth;
//            pastMars = mars;

//            earth = futureEarth;
//            mars = futureMars;
            if(takenOff){
                futureSpaceship = updateStrategy.update(pastSpaceship, spaceship, deltaT, currentTime);
                setAcc(futureSpaceship, List.of(earth,mars,this.sun, jupiter));
                pastSpaceship = spaceship;
                spaceship = futureSpaceship;
                targetMinDistance = Double.min(targetMinDistance,Particle.dist(spaceship,jupiter));
            }

            if(i % step == 0){
                if (takenOff){
                    snapshots.add(new SimulationSnapshot(List.of(spaceship,mars,earth,jupiter,sun), currentTime));
                }else{
                    snapshots.add(new SimulationSnapshot(List.of(earth,mars,jupiter,sun), currentTime));
                }
            }
            currentTime += deltaT;
            i++;
        }
        if (takenOff){
            snapshots.add(new SimulationSnapshot(List.of(spaceship,mars,earth,jupiter,sun), currentTime));
        }else{
            snapshots.add(new SimulationSnapshot(List.of(earth,mars,jupiter,sun), currentTime));
        }
        System.out.printf("Simulation finished at time %.0fs with minimum distance to jupiter = %fkm\n",currentTime,targetMinDistance-jupiter.getRadius());
        return new SpaceMissionResult(config.getTarget(), currentTime,takeOffTime,targetMinDistance - jupiter.getRadius(),isSuccessful(jupiter),initialVelocity,snapshots);
    }

    @Override
    public String getName() {
        return "JupiterMission";
    }


}
