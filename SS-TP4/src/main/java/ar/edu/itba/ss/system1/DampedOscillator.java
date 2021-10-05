package ar.edu.itba.ss.system1;

import java.util.ArrayList;
import java.util.List;

import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.SimulationResult;
import ar.edu.itba.ss.commons.SimulationSnapshot;
import ar.edu.itba.ss.commons.strategies.AnalyticStrategy;
import ar.edu.itba.ss.commons.strategies.BeemanStrategy;
import ar.edu.itba.ss.commons.strategies.EulerStrategy;
import ar.edu.itba.ss.commons.strategies.GearPredictorStrategy;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;
import ar.edu.itba.ss.commons.strategies.VerletOriginalStrategy;

public class DampedOscillator {

    private final UpdateStrategy            updateStrategy;
    private final Config                    config;
    private final List<SimulationSnapshot>  snapshots;

    public DampedOscillator(Config config){
        this.config = config;
        this.updateStrategy = getStrategy(config.getStrategy());
        this.snapshots = new ArrayList<>();
    }

    public DampedOscillator(Config config, String strategy){
        this.config = config;
        this.updateStrategy = getStrategy(strategy);
        this.snapshots = new ArrayList<>();
    }

    private UpdateStrategy getStrategy(String strategy){
        switch(strategy){
            case "verlet":
                return new VerletOriginalStrategy(config);
            case "euler":
                return new EulerStrategy(config);
            case "beeman":
                return new BeemanStrategy(config);

            case "analytic":
                return new AnalyticStrategy().withFunctions(new DampedOscillatorFunctions(config.getR0(), config.getK(), config.getGamma()));
            case "gear":
                return new GearPredictorStrategy(config);
            default:
                throw new IllegalArgumentException();
        }
    }

    public SimulationResult simulate(double deltaT){
        double V0 = -config.getR0() * config.getGamma() / (2*config.getMass());
        double fx = -config.getK() * config.getR0() - config.getGamma() * V0;
        double ax = fx / config.getMass();
        Particle particle = new Particle(0, config.getR0(), 0, 0, config.getMass(), V0, 0, ax, 0);
        Particle past = null;
        Particle future;
        double currentTime = 0;
        double tf = config.getTf();
        int step = config.getStep();
        int i = 0;
        while(currentTime <= tf){
            future = updateStrategy.update(past, particle, deltaT, currentTime);

//            System.out.println("Past: " + past + "\nPresent: " + particle + "\nFuture: " + future);

            if(i % step == 0){
                snapshots.add(new SimulationSnapshot(List.of(particle), currentTime));
            }

            past = particle;
            particle = future;

            currentTime += deltaT;
//            System.out.println("#######################################");
            i++;
        }
        return new SimulationResult(currentTime-deltaT,snapshots,updateStrategy.getName());
    }

    public List<SimulationSnapshot> getSnapshots() {
        return snapshots;
    }
}
