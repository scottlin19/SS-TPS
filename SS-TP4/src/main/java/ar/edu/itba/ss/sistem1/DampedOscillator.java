package ar.edu.itba.ss.sistem1;

import ar.edu.itba.ss.commons.DampedOscillatorFunctions;
import ar.edu.itba.ss.commons.Particle;
import ar.edu.itba.ss.commons.strategies.UpdateStrategy;

public class DampedOscillator {

    private UpdateStrategy updateStrategy;
    private Config config;

    public DampedOscillator(){

    }

    public DampedOscillator(UpdateStrategy updateStrategy, Config config){
        this.updateStrategy = updateStrategy;
        this.config = config;

    }

    public void simulate(){
        Particle particle = new Particle(0, config.getR0(), 0, 0, config.getMass(), config.getV0(), 0, new DampedOscillatorFunctions(config.getR0(), config.getK(),config.getGamma()));
        double currentTime = 0;
        while(currentTime < config.getTf()){
            updateStrategy.update(particle, currentTime);
            currentTime += config.getDeltaT();
        }
    }


    public DampedOscillator withUpdateStrategy(UpdateStrategy updateStrategy){
        this.updateStrategy = updateStrategy;
        return this;
    }

    public DampedOscillator withConfig(Config config){
        this.config = config;
        return this;
    }
}
