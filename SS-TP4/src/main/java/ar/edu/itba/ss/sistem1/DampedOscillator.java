package ar.edu.itba.ss.sistem1;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.ss.commons.DampedOscillatorFunctions;
import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.SystemFunctions;
import ar.edu.itba.ss.commons.strategies.AnalyticStrategy;
import ar.edu.itba.ss.commons.strategies.EulerStrategy;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy;

public class DampedOscillator {

    private final UpdateStrategy            updateStrategy;
    private final Config                    config;
    private final List<SimulationSnapshot>  snapshots;

    public DampedOscillator(Config config){
        this.config = config;
        this.updateStrategy = getStrategy(config);
        this.snapshots = new ArrayList<>();
    }

    private UpdateStrategy getStrategy(Config config){
        switch(config.getStrategy()){
            case "verlet":
                return new VerletOriginalStrategy();

            case "euler":
                return new EulerStrategy();

            case "analytic":
                return new AnalyticStrategy().withFunctions(new DampedOscillatorFunctions(config.getR0(), config.getK(), config.getGamma()));

            default:
                throw new IllegalArgumentException();
        }
    }
    public void simulate(){
        double V0 = -config.getR0() * config.getGamma() / (2*config.getMass());
        System.out.println("V0= "+V0);
        double fx = -config.getK() * config.getR0() - config.getGamma() * V0;
        double ax = fx / config.getMass();
        System.out.println("AX= "+ax);
        System.out.println("fx= "+fx);
        Particle particle = new Particle(0, config.getR0(), 0, 0, config.getMass(), V0, 0, ax, 0);
        Particle past = null;
        Particle future;
        double currentTime = 0;
        double tf = config.getTf();
        while(currentTime <= tf){
            future = updateStrategy.update(past, particle, config.getDeltaT(), currentTime);

            System.out.println("Past: " + past + "\nPresent: " + particle + "\nFuture: " + future);
            snapshots.add(new SimulationSnapshot(List.of(particle), currentTime));
            past = particle;
            particle = future;
            currentTime += config.getDeltaT();
            System.out.println("#######################################");
        }
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }
}
